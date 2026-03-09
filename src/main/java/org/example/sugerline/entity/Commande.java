package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sugerline.enums.StatutCommande;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeLine> commandeLines;
    
    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL)
    private Paiement paiement;

    private Double montantAvantReduction;
    private Double pourcentageReduction;
    private Double montantReduction;
    private Double total;
}
