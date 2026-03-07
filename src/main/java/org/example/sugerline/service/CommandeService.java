package org.example.sugerline.service;

import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;

public interface CommandeService {
    CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO);
}
