package org.example.sugerline.service;

import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitUpdateDto;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProduitService {
    ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO);
    ProduitResponseDTO getProduitById(Long id);
    ProduitResponseDTO updateProduit(Long id, ProduitUpdateDto produitUpdateDto);
    void deleteProduit(Long id);
    Page<ProduitResponseDTO> getAllProduits(String nom, Double minPrice, Double maxPrice, Pageable pageable);
}
