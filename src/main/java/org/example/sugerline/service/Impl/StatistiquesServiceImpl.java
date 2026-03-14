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
import java.time.LocalDateTime;
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
        LocalDate finMoisActuel = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate debutMoisPrecedent = debutMoisActuel.minusMonths(1);
        LocalDate finMoisPrecedent = debutMoisActuel.minusDays(1);

        Long totalCommandes = commandeRepository.count();
        Long totalCommandesEnAttente = commandeRepository.countByStatut(StatutCommande.EN_ATTENTE);
        Long totalCommandesLivrees = commandeRepository.countByStatut(StatutCommande.LIVREE);
        Long totalCommandesAnnulees = commandeRepository.countByStatut(StatutCommande.ANNULEE);

        Long totalPaiements = paiementRepository.count();
        Long totalPaiementsEnAttente = paiementRepository.countByStatut(StatutPaiement.EN_ATTENTE);
        Long totalPaiementsAcceptes = paiementRepository.countByStatut(StatutPaiement.ACCEPTE);
        Long totalPaiementsAnnules = paiementRepository.countByStatut(StatutPaiement.ANNULE);

        Long totalUtilisateurs = utilisateurRepository.count();
        Long totalProduits = produitRepository.count();
        Long totalIngredients = ingredientRepository.count();

        Double revenuTotal = paiementRepository.sumMontantByStatut(StatutPaiement.ACCEPTE);
        Double revenuMoisActuel = paiementRepository.sumMontantByStatutAndDateBetween(
                StatutPaiement.ACCEPTE, debutMoisActuel.atStartOfDay(), finMoisActuel.atTime(23, 59, 59));
        Double revenuMoisPrecedent = paiementRepository.sumMontantByStatutAndDateBetween(
                StatutPaiement.ACCEPTE, debutMoisPrecedent.atStartOfDay(), finMoisPrecedent.atTime(23, 59, 59));

        Double chargesTotal = chargesMensuellesRepository.sumAllCharges();
        Double beneficeNet = (revenuTotal != null ? revenuTotal : 0.0) - (chargesTotal != null ? chargesTotal : 0.0);

        Double tauxCroissanceRevenu = calculerTauxCroissance(revenuMoisPrecedent, revenuMoisActuel);
        
        Long commandesMoisActuel = commandeRepository.countByDateBetween(debutMoisActuel, finMoisActuel);
        Long commandesMoisPrecedent = commandeRepository.countByDateBetween(debutMoisPrecedent, finMoisPrecedent);
        Double tauxCroissanceCommandes = calculerTauxCroissance(commandesMoisPrecedent.doubleValue(), commandesMoisActuel.doubleValue());

        Double tauxConversionPaiement = totalPaiements > 0 
            ? (totalPaiementsAcceptes.doubleValue() / totalPaiements.doubleValue()) * 100 
            : 0.0;

        List<ChartDataDTO> revenusParMois = getRevenusParMois();
        List<ChartDataDTO> commandesParMois = getCommandesParMois();
        List<ChartDataDTO> commandesParStatut = getCommandesParStatut();
        List<ChartDataDTO> paiementsParStatut = getPaiementsParStatut();
        List<ChartDataDTO> topProduits = getTopProduits();
        List<ChartDataDTO> beneficesParMois = getBeneficesParMois();
        List<ChartDataDTO> chargesParMois = getChargesParMois();
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
                .revenuMoisActuel(revenuMoisActuel != null ? revenuMoisActuel : 0.0)
                .revenuMoisPrecedent(revenuMoisPrecedent != null ? revenuMoisPrecedent : 0.0)
                .beneficeNet(beneficeNet)
                .chargesTotal(chargesTotal != null ? chargesTotal : 0.0)
                .revenusParMois(revenusParMois)
                .commandesParMois(commandesParMois)
                .commandesParStatut(commandesParStatut)
                .paiementsParStatut(paiementsParStatut)
                .topProduits(topProduits)
                .beneficesParMois(beneficesParMois)
                .chargesParMois(chargesParMois)
                .utilisateursParRole(utilisateursParRole)
                .tauxCroissanceRevenu(tauxCroissanceRevenu)
                .tauxCroissanceCommandes(tauxCroissanceCommandes)
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

        Double montantTotalDepense = paiementRepository.sumMontantByUtilisateurAndStatut(utilisateur, StatutPaiement.ACCEPTE);
        Double montantMoisActuel = paiementRepository.sumMontantByUtilisateurAndStatutAndDateBetween(
                utilisateur, StatutPaiement.ACCEPTE, debutMoisActuel.atStartOfDay(), finMoisActuel.atTime(23, 59, 59));
        Double montantMoisPrecedent = paiementRepository.sumMontantByUtilisateurAndStatutAndDateBetween(
                utilisateur, StatutPaiement.ACCEPTE, debutMoisPrecedent.atStartOfDay(), finMoisPrecedent.atTime(23, 59, 59));
        Double montantEnAttente = paiementRepository.sumMontantByUtilisateurAndStatut(utilisateur, StatutPaiement.EN_ATTENTE);

        Double tauxCroissanceDepenses = calculerTauxCroissance(montantMoisPrecedent, montantMoisActuel);
        Double moyenneParCommande = totalMesCommandes > 0 && montantTotalDepense != null
                ? montantTotalDepense / totalMesCommandes
                : 0.0;

        List<ChartDataDTO> mesCommandesParMois = getMesCommandesParMois(utilisateur);
        List<ChartDataDTO> mesDepensesParMois = getMesDepensesParMois(utilisateur);
        List<ChartDataDTO> mesCommandesParStatut = getMesCommandesParStatut(utilisateur);
        List<ChartDataDTO> mesPaiementsParStatut = getMesPaiementsParStatut(utilisateur);
        List<ChartDataDTO> mesProduitsPreferes = getMesProduitsPreferes(utilisateur);

        return StatistiquesUtilisateurResponseDTO.builder()
                .totalMesCommandes(totalMesCommandes)
                .totalCommandesEnAttente(totalCommandesEnAttente)
                .totalCommandesLivrees(totalCommandesLivrees)
                .totalCommandesAnnulees(totalCommandesAnnulees)
                .totalMesPaiements(totalMesPaiements)
                .totalPaiementsEnAttente(totalPaiementsEnAttente)
                .totalPaiementsAcceptes(totalPaiementsAcceptes)
                .totalPaiementsAnnules(totalPaiementsAnnules)
                .montantTotalDepense(montantTotalDepense != null ? montantTotalDepense : 0.0)
                .montantMoisActuel(montantMoisActuel != null ? montantMoisActuel : 0.0)
                .montantMoisPrecedent(montantMoisPrecedent != null ? montantMoisPrecedent : 0.0)
                .montantEnAttente(montantEnAttente != null ? montantEnAttente : 0.0)
                .mesCommandesParMois(mesCommandesParMois)
                .mesDepensesParMois(mesDepensesParMois)
                .mesCommandesParStatut(mesCommandesParStatut)
                .mesPaiementsParStatut(mesPaiementsParStatut)
                .mesProduitsPreferes(mesProduitsPreferes)
                .tauxCroissanceDepenses(tauxCroissanceDepenses)
                .moyenneParCommande(moyenneParCommande)
                .build();
    }

    private List<ChartDataDTO> getRevenusParMois() {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDate debut = yearMonth.atDay(1);
            LocalDate fin = yearMonth.atEndOfMonth();
            
            Double revenu = paiementRepository.sumMontantByStatutAndDateBetween(
                    StatutPaiement.ACCEPTE, debut.atStartOfDay(), fin.atTime(23, 59, 59));
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .value(revenu != null ? revenu : 0.0)
                    .build());
        }
        return data;
    }

    private List<ChartDataDTO> getCommandesParMois() {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDate debut = yearMonth.atDay(1);
            LocalDate fin = yearMonth.atEndOfMonth();
            
            Long count = commandeRepository.countByDateBetween(debut, fin);
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .count(count)
                    .value(count.doubleValue())
                    .build());
        }
        return data;
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

    private List<ChartDataDTO> getTopProduits() {
        List<Object[]> results = commandeRepository.findTopProduits();
        
        List<ChartDataDTO> data = new ArrayList<>();
        for (int i = 0; i < Math.min(results.size(), 10); i++) {
            Object[] result = results.get(i);
            data.add(ChartDataDTO.builder()
                    .label((String) result[0])
                    .count(((Number) result[1]).longValue())
                    .value(((Number) result[1]).doubleValue())
                    .build());
        }
        return data;
    }

    private List<ChartDataDTO> getBeneficesParMois() {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            
            Double benefice = rapportMensuelRepository.findBeneficeByMoisAndAnnee(
                    yearMonth.getMonthValue(), yearMonth.getYear());
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .value(benefice != null ? benefice : 0.0)
                    .build());
        }
        return data;
    }

    private List<ChartDataDTO> getChargesParMois() {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            
            Double charges = chargesMensuellesRepository.findTotalByMoisAndAnnee(
                    yearMonth.getMonthValue(), yearMonth.getYear());
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .value(charges != null ? charges : 0.0)
                    .build());
        }
        return data;
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

    private List<ChartDataDTO> getMesCommandesParMois(Utilisateur utilisateur) {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDate debut = yearMonth.atDay(1);
            LocalDate fin = yearMonth.atEndOfMonth();
            
            Long count = commandeRepository.countByUtilisateurAndDateBetween(utilisateur, debut, fin);
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .count(count)
                    .value(count.doubleValue())
                    .build());
        }
        return data;
    }

    private List<ChartDataDTO> getMesDepensesParMois(Utilisateur utilisateur) {
        List<ChartDataDTO> data = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDate debut = yearMonth.atDay(1);
            LocalDate fin = yearMonth.atEndOfMonth();
            
            Double montant = paiementRepository.sumMontantByUtilisateurAndStatutAndDateBetween(
                    utilisateur, StatutPaiement.ACCEPTE, debut.atStartOfDay(), fin.atTime(23, 59, 59));
            
            data.add(ChartDataDTO.builder()
                    .label(yearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + yearMonth.getYear())
                    .value(montant != null ? montant : 0.0)
                    .build());
        }
        return data;
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

    private List<ChartDataDTO> getMesProduitsPreferes(Utilisateur utilisateur) {
        List<Object[]> results = commandeRepository.findTopProduitsByUtilisateur(utilisateur);
        
        List<ChartDataDTO> data = new ArrayList<>();
        for (int i = 0; i < Math.min(results.size(), 5); i++) {
            Object[] result = results.get(i);
            data.add(ChartDataDTO.builder()
                    .label((String) result[0])
                    .count(((Number) result[1]).longValue())
                    .value(((Number) result[1]).doubleValue())
                    .build());
        }
        return data;
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
