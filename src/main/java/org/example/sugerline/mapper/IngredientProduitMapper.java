package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.IngredientProduitRequestDTO;
import org.example.sugerline.dto.request.IngredientProduitUpdateDto;
import org.example.sugerline.dto.response.IngredientProduitResponseDTO;
import org.example.sugerline.entity.IngredientProduit;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IngredientProduitMapper {

    @Mapping(target = "ingredient.id", source = "ingredientId")
    @Mapping(target = "produit", ignore = true)
    IngredientProduit toEntity(IngredientProduitRequestDTO ingredientProduitRequestDTO);

    @Mapping(target = "ingredientNom", source = "ingredient.nom")
    @Mapping(target = "ingredientId", source = "ingredient.id")
    IngredientProduitResponseDTO toResponseDTO(IngredientProduit ingredientProduit);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(IngredientProduitUpdateDto ingredientProduitUpdateDto, @MappingTarget IngredientProduit ingredientProduit);
}
