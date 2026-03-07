package org.example.sugerline.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeUpdateDTO {
    private LocalDate date;
    private List<CommandeLineRequestDTO> commandeLines;
}
