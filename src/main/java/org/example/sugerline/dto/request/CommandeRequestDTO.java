package org.example.sugerline.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequestDTO {

    @NotNull(message = "Les lignes de commande sont obligatoires")
    @Valid
    private List<CommandeLineRequestDTO> commandeLines;
}
