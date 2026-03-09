package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.IngredientProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitUpdateDto;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.example.sugerline.entity.Ingredient;
import org.example.sugerline.entity.IngredientProduit;
import org.example.sugerline.entity.Produit;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.ProduitMapper;
import org.example.sugerline.repository.IngredientRepository;
import org.example.sugerline.repository.ProduitRepository;
import org.example.sugerline.specification.SpecificationBuilder;
import org.example.sugerline.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitMapper produitMapper;
    private final ProduitRepository produitRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional
    public ProduitResponseDTO createProduit(ProduitRequestDTO produitRequestDTO) {
        Produit produit = produitMapper.toEntity(produitRequestDTO);

        if (produitRequestDTO.getIngredientProduits() != null && !produitRequestDTO.getIngredientProduits().isEmpty()) {
            List<IngredientProduit> ingredientProduits = new ArrayList<>();

            for (IngredientProduitRequestDTO ipDto : produitRequestDTO.getIngredientProduits()) {
                Ingredient ingredient = ingredientRepository.findById(ipDto.getIngredientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + ipDto.getIngredientId()));

                IngredientProduit ip = IngredientProduit.builder()
                        .quantite(ipDto.getQuantite())
                        .ingredient(ingredient)
                        .produit(produit)
                        .build();

                ingredientProduits.add(ip);
            }

            produit.setIngredientProduits(ingredientProduits);
        }

        return produitMapper.toResponseDTO(produitRepository.save(produit));
    }

    @Override
    public ProduitResponseDTO getProduitById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit not found with id: " + id));
        return produitMapper.toResponseDTO(produit);
    }

    @Override
    @Transactional
    public ProduitResponseDTO updateProduit(Long id, ProduitUpdateDto produitUpdateDto) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit not found with id: " + id));

        produitMapper.updateEntityFromDTO(produitUpdateDto, produit);

        if (produitUpdateDto.getIngredientProduits() != null) {
            produit.getIngredientProduits().clear();

            for (IngredientProduitRequestDTO ipDto : produitUpdateDto.getIngredientProduits()) {
                Ingredient ingredient = ingredientRepository.findById(ipDto.getIngredientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + ipDto.getIngredientId()));

                IngredientProduit ip = IngredientProduit.builder()
                        .quantite(ipDto.getQuantite())
                        .ingredient(ingredient)
                        .produit(produit)
                        .build();

                produit.getIngredientProduits().add(ip);
            }
        }

        return produitMapper.toResponseDTO(produitRepository.save(produit));
    }

    @Override
    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit not found with id: " + id));
        produitRepository.delete(produit);
    }

    @Override
    public Page<ProduitResponseDTO> getAllProduits(String nom, Double minPrice, Double maxPrice, Pageable pageable) {
        Specification<Produit> spec = SpecificationBuilder.produitSpec(nom, minPrice, maxPrice);
        Page<Produit> page = produitRepository.findAll(spec, pageable);
        return page.map(produitMapper::toResponseDTO);
    }
}
