package org.example.sugerline.service;

import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.request.IngredientUpdateDto;
import org.example.sugerline.dto.response.IngredientResponseDTO;

import java.util.List;

public interface IngredientService {
    IngredientResponseDTO createIngredient(IngredientRequestDTO ingredientRequestDTO);
    IngredientResponseDTO getIngredientById(Long id);
    IngredientResponseDTO updateIngredient(Long id, IngredientUpdateDto ingredientUpdateDto);
    void deleteIngredient(Long id);
    List<IngredientResponseDTO> getAllIngredients();
}
