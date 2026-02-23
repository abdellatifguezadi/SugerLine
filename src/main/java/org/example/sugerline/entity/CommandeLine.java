package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer quantite;
    
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private Double Total;
}
