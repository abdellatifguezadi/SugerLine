package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponseDTO {
    private Long id;
    private LocalDate date;
    private String statut;
    private UtilisateurResponseDTO utilisateur;
    private List<CommandeLineResponseDTO> commandeLines;
    private PaiementResponseDTO paiement;
    private Double montantAvantReduction;
    private Double pourcentageReduction;
    private Double montantReduction;
    private Double total;
}
