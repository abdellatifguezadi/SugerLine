package org.example.sugerline.service;

import org.example.sugerline.dto.response.StatistiquesAdminResponseDTO;
import org.example.sugerline.dto.response.StatistiquesUtilisateurResponseDTO;

public interface StatistiquesService {
    
    StatistiquesAdminResponseDTO getStatistiquesAdmin();
    
    StatistiquesUtilisateurResponseDTO getStatistiquesUtilisateur();
}
