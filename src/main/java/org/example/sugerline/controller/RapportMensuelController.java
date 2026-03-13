package org.example.sugerline.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.response.RapportMensuelResponseDTO;
import org.example.sugerline.service.RapportMensuelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rapports")
@RequiredArgsConstructor
@Validated
public class RapportMensuelController {

    private final RapportMensuelService rapportService;

    @PostMapping("/generer")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<RapportMensuelResponseDTO> genererRapport(
            @RequestParam @Min(1) @Max(12) Integer mois,
            @RequestParam @Min(2020) Integer annee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rapportService.genererRapport(mois, annee));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<RapportMensuelResponseDTO> getRapportById(@PathVariable Long id) {
        return ResponseEntity.ok(rapportService.getRapportById(id));
    }

    @GetMapping("/mois/{mois}/annee/{annee}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<RapportMensuelResponseDTO> getRapportByMoisAndAnnee(
            @PathVariable @Min(1) @Max(12) Integer mois,
            @PathVariable @Min(2020) Integer annee) {
        return ResponseEntity.ok(rapportService.getRapportByMoisAndAnnee(mois, annee));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<RapportMensuelResponseDTO>> getAllRapports(Pageable pageable) {
        return ResponseEntity.ok(rapportService.getAllRapports(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> deleteRapport(@PathVariable Long id) {
        rapportService.deleteRapport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test-auth")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Authentication successful - ADMIN role confirmed");
    }
}
