package org.example.sugerline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequestDTO {

    @NotBlank(message = "Le nom de l'ingrédient est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom de l'ingrédient doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "La quantité est obligatoire")
    private String quantite;

    @NotBlank(message = "L'unité est obligatoire")
    private String unite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private Double prixUnitaire;

    @NotBlank(message = "Le type de l'ingrédient est obligatoire")
    private String type;
}

