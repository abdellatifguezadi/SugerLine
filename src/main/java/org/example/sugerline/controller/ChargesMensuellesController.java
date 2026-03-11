package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.ChargesMensuellesRequestDTO;
import org.example.sugerline.dto.request.ChargesMensuellesUpdateDTO;
import org.example.sugerline.dto.response.ChargesMensuellesResponseDTO;
import org.example.sugerline.service.ChargesMensuellesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charges")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class ChargesMensuellesController {

    private final ChargesMensuellesService chargesService;

    @PostMapping
    public ResponseEntity<ChargesMensuellesResponseDTO> createCharges(
            @Valid @RequestBody ChargesMensuellesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chargesService.createCharges(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargesMensuellesResponseDTO> updateCharges(
            @PathVariable Long id,
            @Valid @RequestBody ChargesMensuellesUpdateDTO dto) {
        return ResponseEntity.ok(chargesService.updateCharges(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargesMensuellesResponseDTO> getChargesById(@PathVariable Long id) {
        return ResponseEntity.ok(chargesService.getChargesById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharges(@PathVariable Long id) {
        chargesService.deleteCharges(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ChargesMensuellesResponseDTO>> getAllCharges(
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            @RequestParam(required = false) String utilisateurUsername,
            Pageable pageable) {
        return ResponseEntity.ok(chargesService.getAllCharges(mois, annee, minTotal, maxTotal, utilisateurUsername, pageable));
    }
}
