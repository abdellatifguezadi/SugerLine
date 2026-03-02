package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitUpdateDto;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.example.sugerline.entity.Produit;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {IngredientProduitMapper.class})
public interface ProduitMapper {

    @Mapping(target = "ingredientProduits", source = "ingredientProduits")
    Produit toEntity(ProduitRequestDTO produitRequestDTO);

    ProduitResponseDTO toResponseDTO(Produit produit);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProduitUpdateDto produitUpdateDto, @MappingTarget Produit produit);
}
