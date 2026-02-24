package org.example.sugerline.mapper;


import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.request.IngredientUpdateDto;
import org.example.sugerline.dto.response.IngredientResponseDTO;
import org.example.sugerline.entity.Ingredient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(IngredientRequestDTO ingredientRequestDTO);

    IngredientResponseDTO toResponseDTO(Ingredient ingredient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(IngredientUpdateDto ingredientUpdateDto, @MappingTarget Ingredient ingredient);
}
