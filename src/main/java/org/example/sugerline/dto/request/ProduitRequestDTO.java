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
public class ProduitRequestDTO {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du produit doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotNull(message = "Le prix de production est obligatoire")
    private Double prixProduction;

    @NotNull(message = "Le prix de vente est obligatoire")
    private Double prixVente;
}

