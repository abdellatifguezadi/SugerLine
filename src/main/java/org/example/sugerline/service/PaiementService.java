package org.example.sugerline.service;

import org.example.sugerline.dto.request.PaiementRequestDTO;
import org.example.sugerline.dto.response.PaiementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaiementService {
    PaiementResponseDTO createPaiement(PaiementRequestDTO dto);
    PaiementResponseDTO accepterPaiement(Long id);
    PaiementResponseDTO annulerPaiement(Long id);
    PaiementResponseDTO getPaiementById(Long id);
    Page<PaiementResponseDTO> getAllPaiements(String statut, Pageable pageable);
    Page<PaiementResponseDTO> getMyPaiements(String statut, Pageable pageable);
}
