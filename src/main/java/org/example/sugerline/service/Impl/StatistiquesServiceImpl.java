package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.response.ChartDataDTO;
import org.example.sugerline.dto.response.StatistiquesAdminResponseDTO;
import org.example.sugerline.dto.response.StatistiquesUtilisateurResponseDTO;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.Role;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.enums.StatutPaiement;
import org.example.sugerline.repository.*;
import org.example.sugerline.service.StatistiquesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatistiquesServiceImpl implements StatistiquesService {

    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProduitRepository produitRepository;
    private final IngredientRepository ingredientRepository;
    private final ChargesMensuellesRepository chargesMensuellesRepository;
    private final RapportMensuelRepository rapportMensuelRepository;

    @Override
    public StatistiquesAdminResponseDTO getStatistiquesAdmin() {
        LocalDate now = LocalDate.now();
        LocalDate debutMoisActuel = now.withDayOfMonth(1);
        LocalDate debutMoisPrecedent = debutMoisActuel.minusMonths(1);

        long totalCommandes = commandeRepository.count();
        long totalCommandesEnAttente = commandeRepository.countByStatut(StatutCommande.EN_ATTENTE);
        long totalCommandesLivrees = commandeRepository.countByStatut(StatutCommande.LIVREE);
        long totalCommandesAnnulees = commandeRepository.countByStatut(StatutCommande.ANNULEE);

        long totalPaiements = paiementRepository.count();
        long totalPaiementsEnAttente = paiementRepository.countByStatut(StatutPaiement.EN_ATTENTE);
        long totalPaiementsAcceptes = paiementRepository.countByStatut(StatutPaiement.ACCEPTE);
        long totalPaiementsAnnules = paiementRepository.countByStatut(StatutPaiement.ANNULE);

        long totalUtilisateurs = utilisateurRepository.count();
        long totalProduits = produitRepository.count();
        long totalIngredients = ingredientRepository.count();

        Double revenuTotal = commandeRepository.sumRevenueByStatut(StatutCommande.LIVREE);
        if (revenuTotal == null) {
            revenuTotal = 0.00;
        }

        Double revenuMoisActuel = commandeRepository.sumRevenueByMoisAndAnnee(StatutCommande.LIVREE, debutMoisActuel.getMonthValue(), debutMoisActuel.getYear());
        if (revenuMoisActuel == null) {
            revenuMoisActuel = rapportMensuelRepository.findByMoisAndAnnee(debutMoisActuel.getMonthValue(), debutMoisActuel.getYear())
                    .map(r -> r.getChiffreAffaires() != null ? r.getChiffreAffaires() : 0.0)
                    .orElse(0.0);
        }

        Double revenuMoisPrecedent = commandeRepository.sumRevenueByMoisAndAnnee(StatutCommande.LIVREE, debutMoisPrecedent.getMonthValue(), debutMoisPrecedent.getYear());
        if (revenuMoisPrecedent == null) {
            revenuMoisPrecedent = rapportMensuelRepository.findByMoisAndAnnee(debutMoisPrecedent.getMonthValue(), debutMoisPrecedent.getYear())
                    .map(r -> r.getChiffreAffaires() != null ? r.getChiffreAffaires() : 0.0)
                    .orElse(0.0);
        }

        Double chargesTotal = chargesMensuellesRepository.sumAllCharges();
        Double beneficeNet = revenuTotal - (chargesTotal != null ? chargesTotal : 0.0);

        Double tauxCroissanceRevenu = calculerTauxCroissance(revenuMoisPrecedent, revenuMoisActuel);


        Double tauxConversionPaiement = totalPaiements > 0
            ? ((double) totalPaiementsAcceptes / (double) totalPaiements) * 100
            : 0.0;

        List<ChartDataDTO> commandesParStatut = getCommandesParStatut();
        List<ChartDataDTO> paiementsParStatut = getPaiementsParStatut();
        List<ChartDataDTO> utilisateursParRole = getUtilisateursParRole();

        return StatistiquesAdminResponseDTO.builder()
                .totalCommandes(totalCommandes)
                .totalCommandesEnAttente(totalCommandesEnAttente)
                .totalCommandesLivrees(totalCommandesLivrees)
                .totalCommandesAnnulees(totalCommandesAnnulees)
                .totalPaiements(totalPaiements)
                .totalPaiementsEnAttente(totalPaiementsEnAttente)
                .totalPaiementsAcceptes(totalPaiementsAcceptes)
                .totalPaiementsAnnules(totalPaiementsAnnules)
                .totalUtilisateurs(totalUtilisateurs)
                .totalProduits(totalProduits)
                .totalIngredients(totalIngredients)
                .revenuTotal(revenuTotal != null ? revenuTotal : 0.0)
                .revenuMoisActuel(revenuMoisActuel)
                .revenuMoisPrecedent(revenuMoisPrecedent)
                .beneficeNet(beneficeNet)
                .chargesTotal(chargesTotal != null ? chargesTotal : 0.0)
                .commandesParStatut(commandesParStatut)
                .paiementsParStatut(paiementsParStatut)
                .utilisateursParRole(utilisateursParRole)
                .tauxCroissanceRevenu(tauxCroissanceRevenu)
                .tauxConversionPaiement(tauxConversionPaiement)
                .build();
    }

    @Override
    public StatistiquesUtilisateurResponseDTO getStatistiquesUtilisateur() {
        Utilisateur utilisateur = getCurrentUser();
        LocalDate now = LocalDate.now();
        LocalDate debutMoisActuel = now.withDayOfMonth(1);
        LocalDate finMoisActuel = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate debutMoisPrecedent = debutMoisActuel.minusMonths(1);
        LocalDate finMoisPrecedent = debutMoisActuel.minusDays(1);

        Long totalMesCommandes = commandeRepository.countByUtilisateur(utilisateur);
        Long totalCommandesEnAttente = commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.EN_ATTENTE);
        Long totalCommandesLivrees = commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.LIVREE);
        Long totalCommandesAnnulees = commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.ANNULEE);

        Long totalMesPaiements = paiementRepository.countByUtilisateur(utilisateur);
        Long totalPaiementsEnAttente = paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.EN_ATTENTE);
        Long totalPaiementsAcceptes = paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.ACCEPTE);
        Long totalPaiementsAnnules = paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.ANNULE);

        Double montantMoisActuel = paiementRepository.sumMontantByUtilisateurAndStatutAndDateBetween(
                utilisateur, StatutPaiement.ACCEPTE, debutMoisActuel.atStartOfDay(), finMoisActuel.atTime(23, 59, 59));
        Double montantMoisPrecedent = paiementRepository.sumMontantByUtilisateurAndStatutAndDateBetween(
                utilisateur, StatutPaiement.ACCEPTE, debutMoisPrecedent.atStartOfDay(), finMoisPrecedent.atTime(23, 59, 59));

        Double tauxCroissanceDepenses = calculerTauxCroissance(montantMoisPrecedent, montantMoisActuel);


        List<ChartDataDTO> mesCommandesParStatut = getMesCommandesParStatut(utilisateur);
        List<ChartDataDTO> mesPaiementsParStatut = getMesPaiementsParStatut(utilisateur);

        return StatistiquesUtilisateurResponseDTO.builder()
                .totalMesCommandes(totalMesCommandes)
                .totalCommandesEnAttente(totalCommandesEnAttente)
                .totalCommandesLivrees(totalCommandesLivrees)
                .totalCommandesAnnulees(totalCommandesAnnulees)
                .totalMesPaiements(totalMesPaiements)
                .totalPaiementsEnAttente(totalPaiementsEnAttente)
                .totalPaiementsAcceptes(totalPaiementsAcceptes)
                .totalPaiementsAnnules(totalPaiementsAnnules)
                .montantMoisActuel(montantMoisActuel != null ? montantMoisActuel : 0.0)
                .montantMoisPrecedent(montantMoisPrecedent != null ? montantMoisPrecedent : 0.0)
                .mesCommandesParStatut(mesCommandesParStatut)
                .mesPaiementsParStatut(mesPaiementsParStatut)
                .tauxCroissanceDepenses(tauxCroissanceDepenses)
                .build();
    }



    private List<ChartDataDTO> getCommandesParStatut() {
        return Arrays.asList(
                ChartDataDTO.builder()
                        .label("En Attente")
                        .count(commandeRepository.countByStatut(StatutCommande.EN_ATTENTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Livrées")
                        .count(commandeRepository.countByStatut(StatutCommande.LIVREE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Annulées")
                        .count(commandeRepository.countByStatut(StatutCommande.ANNULEE))
                        .build()
        );
    }

    private List<ChartDataDTO> getPaiementsParStatut() {
        return Arrays.asList(
                ChartDataDTO.builder()
                        .label("En Attente")
                        .count(paiementRepository.countByStatut(StatutPaiement.EN_ATTENTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Acceptés")
                        .count(paiementRepository.countByStatut(StatutPaiement.ACCEPTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Annulés")
                        .count(paiementRepository.countByStatut(StatutPaiement.ANNULE))
                        .build()
        );
    }






    private List<ChartDataDTO> getUtilisateursParRole() {
        return Arrays.asList(
                ChartDataDTO.builder()
                        .label("Administrateurs")
                        .count(utilisateurRepository.countByRole(Role.ADMINISTRATEUR))
                        .build(),
                ChartDataDTO.builder()
                        .label("Magasins")
                        .count(utilisateurRepository.countByRole(Role.MAGASIN))
                        .build(),
                ChartDataDTO.builder()
                        .label("Caissiers")
                        .count(utilisateurRepository.countByRole(Role.CAISSIER))
                        .build(),
                ChartDataDTO.builder()
                        .label("livreurs")
                        .count(utilisateurRepository.countByRole(Role.LIVREUR))
                        .build()
        );
    }





    private List<ChartDataDTO> getMesCommandesParStatut(Utilisateur utilisateur) {
        return Arrays.asList(
                ChartDataDTO.builder()
                        .label("En Attente")
                        .count(commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.EN_ATTENTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Livrées")
                        .count(commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.LIVREE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Annulées")
                        .count(commandeRepository.countByUtilisateurAndStatut(utilisateur, StatutCommande.ANNULEE))
                        .build()
        );
    }

    private List<ChartDataDTO> getMesPaiementsParStatut(Utilisateur utilisateur) {
        return Arrays.asList(
                ChartDataDTO.builder()
                        .label("En Attente")
                        .count(paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.EN_ATTENTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Acceptés")
                        .count(paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.ACCEPTE))
                        .build(),
                ChartDataDTO.builder()
                        .label("Annulés")
                        .count(paiementRepository.countByUtilisateurAndStatut(utilisateur, StatutPaiement.ANNULE))
                        .build()
        );
    }



    private Double calculerTauxCroissance(Double ancienneValeur, Double nouvelleValeur) {
        if (ancienneValeur == null || ancienneValeur == 0.0) {
            return nouvelleValeur != null && nouvelleValeur > 0 ? 100.0 : 0.0;
        }
        if (nouvelleValeur == null) {
            return -100.0;
        }
        return ((nouvelleValeur - ancienneValeur) / ancienneValeur) * 100;
    }

    private Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
