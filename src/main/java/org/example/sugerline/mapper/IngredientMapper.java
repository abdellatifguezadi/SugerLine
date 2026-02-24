package org.example.sugerline.mapper;


import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.response.IngredientResponseDTO;
import org.example.sugerline.entity.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(IngredientRequestDTO ingredientRequestDTO);

    IngredientResponseDTO toResponseDTO(Ingredient ingredient);
}
