package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementRequestDTO {

    @NotNull(message = "Le montant est obligatoire")
    @Min(value = 0, message = "Le montant doit être positif")
    private Double montant;

    @NotNull(message = "L'ID de la commande est obligatoire")
    private Long commandeId;
}

