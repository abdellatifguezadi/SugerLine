package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeLineResponseDTO {
    private Long id;
    private Integer quantite;
    private ProduitResponseDTO produit;
}

