package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargesMensuellesUpdateDTO {

    @Min(value = 1, message = "Le mois doit être entre 1 et 12")
    @Max(value = 12, message = "Le mois doit être entre 1 et 12")
    private Integer mois;

    @Min(value = 2020, message = "L'année doit être valide")
    private Integer annee;

    @Min(value = 0, message = "Le montant d'électricité doit être positif")
    private Double electricite;

    @Min(value = 0, message = "Le montant d'eau doit être positif")
    private Double eau;

    @Min(value = 0, message = "Le montant des salaires doit être positif")
    private Double salaires;

    @Min(value = 0, message = "Le montant du loyer doit être positif")
    private Double loyer;

    @Min(value = 0, message = "Le montant des autres charges doit être positif")
    private Double autres;
}

