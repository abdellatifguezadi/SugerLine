package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportMensuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer mois;
    private Integer annee;
    private Double chiffreAffaires;
    private Double coutTotal;
    private Double benefice;
    private Double tauxRentabilite;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charges_mensuelles_id")
    private ChargesMensuelles charges;
}
