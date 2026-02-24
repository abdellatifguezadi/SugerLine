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
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String nom;
    private String quantite;
    private String unite;
    private Double prixUnitaire;

    
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<IngredientProduit> ingredientProduits;
}
