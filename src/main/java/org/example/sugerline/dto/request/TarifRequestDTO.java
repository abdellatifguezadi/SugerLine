package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifRequestDTO {

    @NotBlank(message = "Le type d'utilisateur est obligatoire")
    private String typeUtilisateur;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value = 0, message = "Le prix doit être positif")
    private Double prix;

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;
}

