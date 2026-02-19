package org.example.sugerline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "La disponibilité est obligatoire")
    private Boolean disponibilite;
}

