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
    
    private Double montantMoisActuel;
    private Double montantMoisPrecedent;


    private List<ChartDataDTO> mesCommandesParStatut;
    private List<ChartDataDTO> mesPaiementsParStatut;

    private Double tauxCroissanceDepenses;

}
