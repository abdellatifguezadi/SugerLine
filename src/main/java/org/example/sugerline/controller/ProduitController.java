package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitUpdateDto;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.example.sugerline.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitResponseDTO> createProduit(@Valid @RequestBody ProduitRequestDTO produitRequestDTO) {
        ProduitResponseDTO createdProduit = produitService.createProduit(produitRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponseDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitUpdateDto produitUpdateDto) {
        ProduitResponseDTO updatedProduit = produitService.updateProduit(id, produitUpdateDto);
        return ResponseEntity.ok(updatedProduit);
    }

    @GetMapping
    public ResponseEntity<Page<ProduitResponseDTO>> getAllProduits(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {
        Page<ProduitResponseDTO> produits = produitService.getAllProduits(nom, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponseDTO> getProduitById(@PathVariable Long id) {
        ProduitResponseDTO produit = produitService.getProduitById(id);
        return ResponseEntity.ok(produit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}
