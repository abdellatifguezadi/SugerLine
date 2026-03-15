package org.example.sugerline.service;

import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.response.IngredientResponseDTO;
import org.example.sugerline.entity.Ingredient;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.IngredientMapper;
import org.example.sugerline.repository.IngredientRepository;
import org.example.sugerline.service.Impl.IngredientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    private Ingredient ingredient;
    private IngredientRequestDTO ingredientRequestDTO;
    private IngredientResponseDTO ingredientResponseDTO;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setNom("Test Ingredient");

        ingredientRequestDTO = new IngredientRequestDTO();
        ingredientRequestDTO.setNom("Test Ingredient");

        ingredientResponseDTO = new IngredientResponseDTO();
        ingredientResponseDTO.setId(1L);
        ingredientResponseDTO.setNom("Test Ingredient");
    }

    @Test
    void testCreateIngredient() {
        when(ingredientMapper.toEntity(any(IngredientRequestDTO.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toResponseDTO(any(Ingredient.class))).thenReturn(ingredientResponseDTO);

        IngredientResponseDTO createdIngredient = ingredientService.createIngredient(ingredientRequestDTO);

        assertNotNull(createdIngredient);
        assertEquals(ingredientResponseDTO.getId(), createdIngredient.getId());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void testGetIngredientById() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toResponseDTO(any(Ingredient.class))).thenReturn(ingredientResponseDTO);

        IngredientResponseDTO foundIngredient = ingredientService.getIngredientById(1L);

        assertNotNull(foundIngredient);
        assertEquals(ingredientResponseDTO.getId(), foundIngredient.getId());
    }

    @Test
    void testGetIngredientById_NotFound() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ingredientService.getIngredientById(1L));
    }

    @Test
    void testGetAllIngredients() {
        when(ingredientRepository.findAll()).thenReturn(Collections.singletonList(ingredient));
        when(ingredientMapper.toResponseDTO(any(Ingredient.class))).thenReturn(ingredientResponseDTO);

        List<IngredientResponseDTO> ingredients = ingredientService.getAllIngredients();

        assertNotNull(ingredients);
        assertFalse(ingredients.isEmpty());
        assertEquals(1, ingredients.size());
    }
}

