package org.example.sugerline.service;

import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.request.CommandeUpdateDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CommandeService {
    CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO);
    Page<CommandeResponseDTO> getAllCommande(String statut, Long utilisateurId, LocalDate from, LocalDate to, Double minTotal, Double maxTotal, Pageable pageable);
    CommandeResponseDTO getCommandeById(Long id);
    CommandeResponseDTO annulerCommande(Long id);
    CommandeResponseDTO livrerCommande(Long id);
    CommandeResponseDTO updateCommande(Long id, CommandeUpdateDTO commandeUpdateDTO);
    Page<CommandeResponseDTO> getMyCommandes(String statut, LocalDate from, LocalDate to, Double minTotal, Double maxTotal, Pageable pageable);
}
