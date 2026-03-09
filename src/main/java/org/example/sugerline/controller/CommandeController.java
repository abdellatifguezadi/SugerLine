package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.request.CommandeUpdateDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.example.sugerline.service.CommandeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponseDTO> createCommande(@Valid @RequestBody CommandeRequestDTO commandeRequestDTO) {
        CommandeResponseDTO response = commandeService.createCommande(commandeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Page<CommandeResponseDTO>> getAllCommandes(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Long utilisateurId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            Pageable pageable
    ){
        Page<CommandeResponseDTO> commandes = commandeService.getAllCommande(statut, utilisateurId, from, to, minTotal, maxTotal, pageable);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommandeById(@PathVariable Long id){
        CommandeResponseDTO commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commande);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<CommandeResponseDTO> annulerCommande(@PathVariable Long id){
        CommandeResponseDTO commande = commandeService.annulerCommande(id);
        return ResponseEntity.ok(commande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> updateCommande(@PathVariable Long id, @Valid @RequestBody CommandeUpdateDTO commandeUpdateDTO){
        CommandeResponseDTO commande = commandeService.updateCommande(id, commandeUpdateDTO);
        return ResponseEntity.ok(commande);
    }

    @GetMapping("/my-commandes")
    public ResponseEntity<Page<CommandeResponseDTO>> getMyCommandes(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            Pageable pageable
    ){
        Page<CommandeResponseDTO> commandes = commandeService.getMyCommandes(statut, from, to, minTotal, maxTotal, pageable);
        return ResponseEntity.ok(commandes);
    }
}
