package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitUpdateDto {

    @Size(min = 2, max = 100, message = "Le nom du produit doit contenir entre 2 et 100 caractères")
    private String nom;

    private Double prixProduction;

    private Double prixVente;

    @Valid
    private List<IngredientProduitRequestDTO> ingredientProduits;
}