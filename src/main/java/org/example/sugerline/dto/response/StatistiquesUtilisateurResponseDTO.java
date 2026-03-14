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
public class StatistiquesUtilisateurResponseDTO {
    
    private Long totalMesCommandes;
    private Long totalCommandesEnAttente;
    private Long totalCommandesLivrees;
    private Long totalCommandesAnnulees;
    
    private Long totalMesPaiements;
    private Long totalPaiementsEnAttente;
    private Long totalPaiementsAcceptes;
    private Long totalPaiementsAnnules;
    
    private Double montantTotalDepense;
    private Double montantMoisActuel;
    private Double montantMoisPrecedent;
    private Double montantEnAttente;
    
    private List<ChartDataDTO> mesCommandesParMois;
    private List<ChartDataDTO> mesDepensesParMois;
    private List<ChartDataDTO> mesCommandesParStatut;
    private List<ChartDataDTO> mesPaiementsParStatut;
    private List<ChartDataDTO> mesProduitsPreferes;
    
    private Double tauxCroissanceDepenses;
    private Double moyenneParCommande;
}
