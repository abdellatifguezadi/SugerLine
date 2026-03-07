package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.request.CommandeUpdateDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.example.sugerline.entity.Commande;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CommandeLineMapper.class, UtilisateurMapper.class})
public interface CommandeMapper {

    Commande toEntity(CommandeRequestDTO commandeRequestDTO);

    CommandeResponseDTO toResponseDTO(Commande commande);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CommandeUpdateDTO commandeUpdateDTO, @MappingTarget Commande commande);
}
