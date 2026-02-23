package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargesMensuellesResponseDTO {
    private Long id;
    private Integer mois;
    private Integer annee;
    private Double electricite;
    private Double eau;
    private Double salaires;
    private Double loyer;
    private Double autres;
    private Double total;
}

