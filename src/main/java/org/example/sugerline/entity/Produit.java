package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private Double prixProduction;
    private Double prixVente;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<IngredientProduit> ingredientProduits;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<CommandeLine> commandeLines;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<Tarif> tarifs;
}
