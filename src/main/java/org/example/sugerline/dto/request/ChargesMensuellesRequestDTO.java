package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargesMensuellesRequestDTO {

    @NotNull(message = "Le mois est obligatoire")
    @Min(value = 1, message = "Le mois doit être entre 1 et 12")
    @Max(value = 12, message = "Le mois doit être entre 1 et 12")
    private Integer mois;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 2020, message = "L'année doit être valide")
    private Integer annee;

    @NotNull(message = "Le montant d'électricité est obligatoire")
    @Min(value = 0, message = "Le montant d'électricité doit être positif")
    private Double electricite;

    @NotNull(message = "Le montant d'eau est obligatoire")
    @Min(value = 0, message = "Le montant d'eau doit être positif")
    private Double eau;

    @NotNull(message = "Le montant des salaires est obligatoire")
    @Min(value = 0, message = "Le montant des salaires doit être positif")
    private Double salaires;

    @NotNull(message = "Le montant du loyer est obligatoire")
    @Min(value = 0, message = "Le montant du loyer doit être positif")
    private Double loyer;

    @Min(value = 0, message = "Le montant des autres charges doit être positif")
    private Double autres;
}
