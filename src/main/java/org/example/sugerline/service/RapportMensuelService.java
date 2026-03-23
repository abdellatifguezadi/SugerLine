package org.example.sugerline.service;

import org.example.sugerline.dto.response.RapportMensuelResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RapportMensuelService {
    RapportMensuelResponseDTO genererRapport(Integer mois, Integer annee);
    RapportMensuelResponseDTO getRapportById(Long id);
    Page<RapportMensuelResponseDTO> getAllRapports(Pageable pageable);
    void deleteRapport(Long id);
}
