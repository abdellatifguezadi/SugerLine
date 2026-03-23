package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesAdminResponseDTO {
    
    private Long totalCommandes;
    private Long totalCommandesEnAttente;
    private Long totalCommandesLivrees;
    private Long totalCommandesAnnulees;
    
    private Long totalPaiements;
    private Long totalPaiementsEnAttente;
    private Long totalPaiementsAcceptes;
    private Long totalPaiementsAnnules;
    
    private Long totalUtilisateurs;
    private Long totalProduits;
    private Long totalIngredients;
    
    private Double revenuTotal;
    private Double revenuMoisActuel;
    private Double revenuMoisPrecedent;
    private Double beneficeNet;
    private Double chargesTotal;

    private List<ChartDataDTO> commandesParStatut;
    private List<ChartDataDTO> paiementsParStatut;
    private List<ChartDataDTO> utilisateursParRole;
    
    private Double tauxCroissanceRevenu;
    private Double tauxConversionPaiement;
}
