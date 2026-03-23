package org.example.sugerline.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequestDTO {

    @NotNull(message = "La date est obligatoire")
    @FutureOrPresent(message = "La date doit être aujourd'hui ou une date future")
    private LocalDate date;

    private Long utilisateurId;

    @NotNull(message = "Les lignes de commande sont obligatoires")
    @Valid
    private List<CommandeLineRequestDTO> commandeLines;
}
