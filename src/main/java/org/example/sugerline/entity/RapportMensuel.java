package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportMensuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer mois;
    private Integer annee;
    private Double chiffreAffaires;
    private Double coutProduction;
    private Double chargesFixes;
    private Double coutTotal;
    private Double benefice;
    private Double tauxRentabilite;
}
