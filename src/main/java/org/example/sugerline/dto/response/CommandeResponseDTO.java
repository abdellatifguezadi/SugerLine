package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponseDTO {
    private Long id;
    private LocalDateTime date;
    private String source;
    private String statut;
    private UtilisateurResponseDTO utilisateur;
    private List<CommandeLineResponseDTO> commandeLines;
    private PaiementResponseDTO paiement;
    private Double montantAvantReduction;
    private Double pourcentageReduction;
    private Double montantReduction;
    private Double total;
}
