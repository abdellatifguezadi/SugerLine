package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDTO {
    private Long id;
    private String type;
    private String nom;
    private String quantite;
    private String unite;
    private Double prixUnitaire;

}

