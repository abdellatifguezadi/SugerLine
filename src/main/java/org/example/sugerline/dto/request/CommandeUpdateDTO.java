package org.example.sugerline.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeUpdateDTO {
    @FutureOrPresent(message = "La date doit être aujourd'hui ou une date future")
    private LocalDate date;
    private List<CommandeLineRequestDTO> commandeLines;
}
