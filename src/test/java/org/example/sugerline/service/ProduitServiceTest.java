package org.example.sugerline.service;

import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.example.sugerline.entity.Produit;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.ProduitMapper;
import org.example.sugerline.repository.CommandeLineRepository;
import org.example.sugerline.repository.IngredientRepository;
import org.example.sugerline.repository.ProduitRepository;
import org.example.sugerline.service.Impl.ProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ProduitMapper produitMapper;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CommandeLineRepository commandeLineRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit produit;
    private ProduitRequestDTO produitRequestDTO;
    private ProduitResponseDTO produitResponseDTO;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Test Produit");

        produitRequestDTO = new ProduitRequestDTO();
        produitRequestDTO.setNom("Test Produit");

        produitResponseDTO = new ProduitResponseDTO();
        produitResponseDTO.setId(1L);
        produitResponseDTO.setNom("Test Produit");
    }

    @Test
    void testCreateProduit() {
        when(produitMapper.toEntity(any(ProduitRequestDTO.class))).thenReturn(produit);
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(produitMapper.toResponseDTO(any(Produit.class))).thenReturn(produitResponseDTO);

        ProduitResponseDTO createdProduit = produitService.createProduit(produitRequestDTO);

        assertNotNull(createdProduit);
        assertEquals(produitResponseDTO.getId(), createdProduit.getId());
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    void testGetProduitById() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitMapper.toResponseDTO(any(Produit.class))).thenReturn(produitResponseDTO);

        ProduitResponseDTO foundProduit = produitService.getProduitById(1L);

        assertNotNull(foundProduit);
        assertEquals(produitResponseDTO.getId(), foundProduit.getId());
    }

    @Test
    void testGetProduitById_NotFound() {
        when(produitRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> produitService.getProduitById(1L));
    }

    @Test
    void testDeleteProduit() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(commandeLineRepository.existsByProduitId(1L)).thenReturn(false);

        produitService.deleteProduit(1L);

        verify(produitRepository, times(1)).delete(produit);
    }

    @Test
    void testDeleteProduit_NotFound() {
        when(produitRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> produitService.deleteProduit(1L));
    }
}

