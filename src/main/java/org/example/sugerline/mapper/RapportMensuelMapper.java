package org.example.sugerline.mapper;

import org.example.sugerline.dto.response.RapportMensuelResponseDTO;
import org.example.sugerline.entity.RapportMensuel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ChargesMensuellesMapper.class})
public interface RapportMensuelMapper {
    RapportMensuelResponseDTO toResponseDTO(RapportMensuel rapportMensuel);
}
