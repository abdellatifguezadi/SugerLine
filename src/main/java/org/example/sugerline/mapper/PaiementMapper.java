package org.example.sugerline.mapper;

import org.example.sugerline.dto.request.PaiementRequestDTO;
import org.example.sugerline.dto.response.PaiementResponseDTO;
import org.example.sugerline.entity.Paiement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "commande", ignore = true)
    Paiement toEntity(PaiementRequestDTO dto);

    @Mapping(source = "commande.id",            target = "commandeId")
    @Mapping(source = "commande.total",         target = "commandeTotal")
    @Mapping(source = "commande.statut",        target = "commandeStatut")
    @Mapping(source = "commande.utilisateur.username", target = "utilisateurUsername")
    PaiementResponseDTO toResponseDTO(Paiement paiement);
}

