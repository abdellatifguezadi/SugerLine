package org.example.sugerline.service;

import org.example.sugerline.dto.request.ChargesMensuellesRequestDTO;
import org.example.sugerline.dto.request.ChargesMensuellesUpdateDTO;
import org.example.sugerline.dto.response.ChargesMensuellesResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChargesMensuellesService {
    ChargesMensuellesResponseDTO createCharges(ChargesMensuellesRequestDTO dto);
    ChargesMensuellesResponseDTO updateCharges(Long id, ChargesMensuellesUpdateDTO dto);
    ChargesMensuellesResponseDTO getChargesById(Long id);
    void deleteCharges(Long id);
    Page<ChargesMensuellesResponseDTO> getAllCharges(Integer mois, Integer annee, Double minTotal, Double maxTotal, String utilisateurUsername, Pageable pageable);
}
