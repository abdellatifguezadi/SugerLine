package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private Long id;
    private String contenu;
    private LocalDateTime date;
    private String expediteur;
    private Long utilisateurId;
    private String utilisateurUsername;
}

