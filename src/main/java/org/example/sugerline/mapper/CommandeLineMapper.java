package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.CommandeLineRequestDTO;
import org.example.sugerline.dto.response.CommandeLineResponseDTO;
import org.example.sugerline.entity.CommandeLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeLineMapper {

    @Mapping(target = "produit.id", source = "produitId")
    CommandeLine toEntity(CommandeLineRequestDTO commandeLineRequestDTO);

    @Mapping(target = "produit", source = "produit.nom")
    CommandeLineResponseDTO toResponseDTO(CommandeLine commandeLine);
}
