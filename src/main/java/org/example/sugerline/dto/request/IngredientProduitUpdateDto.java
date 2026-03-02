package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientProduitUpdateDto {

    @Min(value = 0, message = "La quantité doit être positive")
    private Double quantite;

    private Long produitId;

    private Long ingredientId;
}