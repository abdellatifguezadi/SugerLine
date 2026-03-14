package org.example.sugerline.controller;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.response.StatistiquesAdminResponseDTO;
import org.example.sugerline.dto.response.StatistiquesUtilisateurResponseDTO;
import org.example.sugerline.service.StatistiquesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class StatistiquesController {

    private final StatistiquesService statistiquesService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<StatistiquesAdminResponseDTO> getStatistiquesAdmin() {
        return ResponseEntity.ok(statistiquesService.getStatistiquesAdmin());
    }

    @GetMapping("/mes-statistiques")
    public ResponseEntity<StatistiquesUtilisateurResponseDTO> getMesStatistiques() {
        return ResponseEntity.ok(statistiquesService.getStatistiquesUtilisateur());
    }
}
