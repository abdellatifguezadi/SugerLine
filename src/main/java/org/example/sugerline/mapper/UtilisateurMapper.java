package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    Utilisateur toEntity(RegisterRequestDTO registerRequestDTO);

    AuthResponseDTO toAuthResponseDTO(Utilisateur utilisateur);
}

