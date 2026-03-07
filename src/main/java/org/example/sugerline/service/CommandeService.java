package org.example.sugerline.service;

import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;

import java.util.List;

public interface CommandeService {
    CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO);
    List<CommandeResponseDTO> getAllCommande();
    CommandeResponseDTO getCommandeById(Long id);
    CommandeResponseDTO annulerCommande(Long id);
}
