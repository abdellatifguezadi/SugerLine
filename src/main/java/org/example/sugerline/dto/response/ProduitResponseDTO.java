package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponseDTO {
    private Long id;
    private String nom;
    private Double prixProduction;
    private Double prixVente;
    private List<IngredientProduitResponseDTO> ingredientProduits;
}

