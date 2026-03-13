package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.RapportMensuelRequestDTO;
import org.example.sugerline.dto.response.RapportMensuelResponseDTO;
import org.example.sugerline.entity.RapportMensuel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RapportMensuelMapper {
    RapportMensuelResponseDTO toResponseDTO(RapportMensuel rapportMensuel);
}
