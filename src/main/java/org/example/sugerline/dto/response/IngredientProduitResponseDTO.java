package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientProduitResponseDTO {
    private Long id;
    private Double quantite;
    private String ingredientNom;
    private Long ingredientId;
}

