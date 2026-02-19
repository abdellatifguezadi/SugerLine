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
public class RapportMensuelRequestDTO {

    @NotNull(message = "Le mois est obligatoire")
    @Min(value = 1, message = "Le mois doit être entre 1 et 12")
    @Max(value = 12, message = "Le mois doit être entre 1 et 12")
    private Integer mois;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 2020, message = "L'année doit être valide")
    private Integer annee;

    @NotNull(message = "Le chiffre d'affaires est obligatoire")
    @Min(value = 0, message = "Le chiffre d'affaires doit être positif")
    private Double chiffreAffaires;

    @NotNull(message = "Le coût de production est obligatoire")
    @Min(value = 0, message = "Le coût de production doit être positif")
    private Double coutProduction;

    @NotNull(message = "Les charges fixes sont obligatoires")
    @Min(value = 0, message = "Les charges fixes doivent être positives")
    private Double chargesFixes;

    @NotNull(message = "Le coût total est obligatoire")
    @Min(value = 0, message = "Le coût total doit être positif")
    private Double coutTotal;

    @NotNull(message = "Le bénéfice est obligatoire")
    private Double benefice;

    @NotNull(message = "Le taux de rentabilité est obligatoire")
    private Double tauxRentabilite;
}
