package org.example.sugerline.service.Impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.response.IngredientResponseDTO;
import org.example.sugerline.entity.Ingredient;
import org.example.sugerline.mapper.IngredientMapper;
import org.example.sugerline.repository.IngredientRepository;
import org.example.sugerline.service.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;


    @Override
    public IngredientResponseDTO createIngredient(IngredientRequestDTO ingredientRequestDTO) {
        Ingredient ingredient = ingredientMapper.toEntity(ingredientRequestDTO);

        return ingredientMapper.toResponseDTO(ingredientRepository.save(ingredient));
    }

    @Override
    public IngredientResponseDTO getIngredientById(Long id) {
        return null;
    }

    @Override
    public IngredientResponseDTO updateIngredient(Long id, IngredientRequestDTO ingredientRequestDTO) {
        return null;
    }

    @Override
    public void deleteIngredient(Long id) {

    }

    @Override
    public List<IngredientResponseDTO> getAllIngredients() {
        return List.of();
    }
}
