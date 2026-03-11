package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.ChargesMensuellesRequestDTO;
import org.example.sugerline.dto.request.ChargesMensuellesUpdateDTO;
import org.example.sugerline.dto.response.ChargesMensuellesResponseDTO;
import org.example.sugerline.entity.ChargesMensuelles;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ChargesMensuellesMapper {


    ChargesMensuelles toEntity(ChargesMensuellesRequestDTO dto);

    @Mapping(source = "utilisateur.username", target = "utilisateurUsername")
    ChargesMensuellesResponseDTO toResponseDTO(ChargesMensuelles chargesMensuelles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ChargesMensuellesUpdateDTO dto, @MappingTarget ChargesMensuelles chargesMensuelles);
}
