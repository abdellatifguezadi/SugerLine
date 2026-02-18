package org.example.sugerline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargesMensuelles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer mois;
    private Integer annee;
    private Double electricite;
    private Double eau;
    private Double salaires;
    private Double loyer;
    private Double autres;
    private Double total;
}
