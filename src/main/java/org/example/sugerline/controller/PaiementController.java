package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.PaiementRequestDTO;
import org.example.sugerline.dto.response.PaiementResponseDTO;
import org.example.sugerline.service.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<PaiementResponseDTO> createPaiement(
            @Valid @RequestBody PaiementRequestDTO dto) {
        PaiementResponseDTO response = paiementService.createPaiement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/accepter")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<PaiementResponseDTO> accepterPaiement(@PathVariable Long id) {
        PaiementResponseDTO response = paiementService.accepterPaiement(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<PaiementResponseDTO> annulerPaiement(@PathVariable Long id) {
        PaiementResponseDTO response = paiementService.annulerPaiement(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementResponseDTO> getPaiementById(@PathVariable Long id) {
        PaiementResponseDTO response = paiementService.getPaiementById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<PaiementResponseDTO>> getAllPaiements(
            @RequestParam(required = false) String statut,
            Pageable pageable) {
        Page<PaiementResponseDTO> paiements = paiementService.getAllPaiements(statut, pageable);
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/my-paiements")
    public ResponseEntity<Page<PaiementResponseDTO>> getMyPaiements(
            @RequestParam(required = false) String statut,
            Pageable pageable) {
        Page<PaiementResponseDTO> paiements = paiementService.getMyPaiements(statut, pageable);
        return ResponseEntity.ok(paiements);
    }
}
