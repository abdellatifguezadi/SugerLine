package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.response.RapportMensuelResponseDTO;
import org.example.sugerline.entity.ChargesMensuelles;
import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.RapportMensuel;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.exception.InvalidOperationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.RapportMensuelMapper;
import org.example.sugerline.repository.ChargesMensuellesRepository;
import org.example.sugerline.repository.CommandeRepository;
import org.example.sugerline.repository.RapportMensuelRepository;
import org.example.sugerline.service.RapportMensuelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RapportMensuelServiceImpl implements RapportMensuelService {

    private final RapportMensuelRepository rapportRepository;
    private final CommandeRepository commandeRepository;
    private final ChargesMensuellesRepository chargesRepository;
    private final RapportMensuelMapper rapportMapper;

    @Override
    @Transactional
    public RapportMensuelResponseDTO genererRapport(Integer mois, Integer annee) {
        if (isFutureMonthYear(mois, annee)) {
            throw new InvalidOperationException("Impossible de générer un rapport pour une date future : " + mois + "/" + annee);
        }

        if (rapportRepository.existsByMoisAndAnnee(mois, annee)) {
            throw new InvalidOperationException("Un rapport existe déjà pour " + mois + "/" + annee);
        }

        List<Commande> commandesLivrees = commandeRepository.findByMoisAndAnneeAndStatut(mois, annee, StatutCommande.LIVREE);
        
        Double chiffreAffaires = calculerChiffreAffaires(commandesLivrees);
        Double coutProduction = calculerCoutProduction(commandesLivrees);
        Double chargesFixes = getChargesFixes(mois, annee);
        Double coutTotal = coutProduction + chargesFixes;
        Double benefice = arrondir(chiffreAffaires - coutTotal);
        Double tauxRentabilite = arrondir(calculerTauxRentabilite(benefice, coutTotal));

        ChargesMensuelles chargesEntity = chargesRepository.findByMoisAndAnnee(mois, annee).orElse(null);

        RapportMensuel rapport = RapportMensuel.builder()
                .mois(mois)
                .annee(annee)
                .chiffreAffaires(chiffreAffaires)
                .coutTotal(coutTotal)
                .benefice(benefice)
                .tauxRentabilite(tauxRentabilite)
                .charges(chargesEntity)
                .build();

        return rapportMapper.toResponseDTO(rapportRepository.save(rapport));
    }

    private boolean isFutureMonthYear(Integer mois, Integer annee) {
        if (mois == null || annee == null) return false;
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        if (annee > currentYear) return true;
        if (annee.equals(currentYear) && mois > currentMonth) return true;
        return false;
    }

    @Override
    public RapportMensuelResponseDTO getRapportById(Long id) {
        return rapportMapper.toResponseDTO(
            rapportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rapport non trouvé avec l'ID: " + id))
        );
    }



    @Override
    public Page<RapportMensuelResponseDTO> getAllRapports(Pageable pageable) {
        return rapportRepository.findAll(pageable).map(rapportMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public void deleteRapport(Long id) {
        if (!rapportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rapport non trouvé avec l'ID: " + id);
        }
        rapportRepository.deleteById(id);
    }

    private Double calculerChiffreAffaires(List<Commande> commandes) {
        return commandes.stream()
                .map(Commande::getTotal)
                .filter(total -> total != null)
                .reduce(0.0, Double::sum);
    }

    private Double calculerCoutProduction(List<Commande> commandes) {
        return commandes.stream()
                .flatMap(commande -> commande.getCommandeLines().stream())
                .mapToDouble(commandeLine -> {
                    Double prixProduction = commandeLine.getProduit().getPrixProduction();
                    Integer quantite = commandeLine.getQuantite();
                    return (prixProduction != null ? prixProduction : 0.0) * (quantite != null ? quantite : 0);
                })
                .sum();
    }

    private Double getChargesFixes(Integer mois, Integer annee) {
        return chargesRepository.findByMoisAndAnnee(mois, annee)
            .map(charges -> charges.getElectricite() + charges.getEau() + 
                           charges.getSalaires() + charges.getLoyer() + 
                           (charges.getAutres() != null ? charges.getAutres() : 0.0))
            .orElse(0.0);
    }

    private Double calculerTauxRentabilite(Double benefice, Double coutTotal) {
        if (coutTotal == 0.0) return 0.0;
        return (benefice / coutTotal) * 100;
    }

    private Double arrondir(Double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
