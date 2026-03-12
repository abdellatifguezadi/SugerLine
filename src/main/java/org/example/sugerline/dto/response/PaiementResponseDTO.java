package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sugerline.enums.StatutPaiement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementResponseDTO {
    private Long id;
    private Double montant;
    private LocalDateTime date;
    private StatutPaiement statut;
    private Long commandeId;
    private Double commandeTotal;
    private String commandeStatut;
    private String utilisateurUsername;
}
