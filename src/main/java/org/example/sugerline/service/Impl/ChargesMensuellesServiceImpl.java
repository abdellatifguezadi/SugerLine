package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.ChargesMensuellesRequestDTO;
import org.example.sugerline.dto.request.ChargesMensuellesUpdateDTO;
import org.example.sugerline.dto.response.ChargesMensuellesResponseDTO;
import org.example.sugerline.entity.ChargesMensuelles;
import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.exception.InvalidOperationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.ChargesMensuellesMapper;
import org.example.sugerline.repository.ChargesMensuellesRepository;
import org.example.sugerline.repository.CommandeRepository;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.service.ChargesMensuellesService;
import org.example.sugerline.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargesMensuellesServiceImpl implements ChargesMensuellesService {

    private final ChargesMensuellesRepository chargesRepository;
    private final ChargesMensuellesMapper chargesMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final CommandeRepository commandeRepository;

    @Override
    @Transactional
    public ChargesMensuellesResponseDTO createCharges(ChargesMensuellesRequestDTO dto) {
        if (chargesRepository.existsByMoisAndAnnee(dto.getMois(), dto.getAnnee())) {
            throw new InvalidOperationException(
                "Des charges existent déjà pour le mois " + dto.getMois() + "/" + dto.getAnnee()
            );
        }
        ChargesMensuelles charges = chargesMapper.toEntity(dto);
        charges.setUtilisateur(getCurrentUser());
        charges.setChargesVariables(calculerChargesVariables(dto.getMois(), dto.getAnnee()));
        charges.setTotal(calculerTotal(
            charges.getChargesVariables(), dto.getElectricite(), dto.getEau(),
            dto.getSalaires(), dto.getLoyer(), dto.getAutres()
        ));
        return chargesMapper.toResponseDTO(chargesRepository.save(charges));
    }

    @Override
    @Transactional
    public ChargesMensuellesResponseDTO updateCharges(Long id, ChargesMensuellesUpdateDTO dto) {
        ChargesMensuelles charges = chargesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charges non trouvées avec l'ID: " + id));

        if (dto.getMois() != null || dto.getAnnee() != null) {
            Integer newMois = dto.getMois() != null ? dto.getMois() : charges.getMois();
            Integer newAnnee = dto.getAnnee() != null ? dto.getAnnee() : charges.getAnnee();
            if ((!newMois.equals(charges.getMois()) || !newAnnee.equals(charges.getAnnee()))
                    && chargesRepository.existsByMoisAndAnnee(newMois, newAnnee)) {
                throw new InvalidOperationException(
                    "Des charges existent déjà pour le mois " + newMois + "/" + newAnnee
                );
            }
        }

        chargesMapper.updateEntityFromDTO(dto, charges);
        charges.setChargesVariables(calculerChargesVariables(charges.getMois(), charges.getAnnee()));
        charges.setTotal(calculerTotal(
            charges.getChargesVariables(), charges.getElectricite(), charges.getEau(),
            charges.getSalaires(), charges.getLoyer(), charges.getAutres()
        ));
        return chargesMapper.toResponseDTO(chargesRepository.save(charges));
    }

    @Override
    public ChargesMensuellesResponseDTO getChargesById(Long id) {
        return chargesMapper.toResponseDTO(
            chargesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Charges non trouvées avec l'ID: " + id))
        );
    }

    @Override
    @Transactional
    public void deleteCharges(Long id) {
        if (!chargesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Charges non trouvées avec l'ID: " + id);
        }
        chargesRepository.deleteById(id);
    }

    @Override
    public Page<ChargesMensuellesResponseDTO> getAllCharges(Integer mois, Integer annee, Double minTotal, Double maxTotal, String utilisateurUsername, Pageable pageable) {
        Specification<ChargesMensuelles> spec = SpecificationBuilder.chargesSpec(mois, annee, minTotal, maxTotal, utilisateurUsername);
        return chargesRepository.findAll(spec, pageable).map(chargesMapper::toResponseDTO);
    }

    private Utilisateur getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    private Double calculerChargesVariables(Integer mois, Integer annee) {
        List<Commande> commandesLivrees = commandeRepository.findByMoisAndAnneeAndStatut(mois, annee, StatutCommande.LIVREE);
        
        return commandesLivrees.stream()
                .flatMap(commande -> commande.getCommandeLines().stream())
                .mapToDouble(commandeLine -> {
                    Double prixProduction = commandeLine.getProduit().getPrixProduction();
                    Integer quantite = commandeLine.getQuantite();
                    return (prixProduction != null ? prixProduction : 0.0) * (quantite != null ? quantite : 0);
                })
                .sum();
    }

    private Double calculerTotal(Double chargesVariables, Double electricite, Double eau, Double salaires, Double loyer, Double autres) {
        double total = 0.0;
        if (chargesVariables != null) total += chargesVariables;
        if (electricite != null) total += electricite;
        if (eau != null) total += eau;
        if (salaires != null) total += salaires;
        if (loyer != null) total += loyer;
        if (autres != null) total += autres;
        return total;
    }
}
