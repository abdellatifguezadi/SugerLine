package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportMensuelResponseDTO {
    private Long id;
    private Integer mois;
    private Integer annee;
    private Double chiffreAffaires;
    private Double coutProduction;
    private Double chargesFixes;
    private Double coutTotal;
    private Double benefice;
    private Double tauxRentabilite;
}


