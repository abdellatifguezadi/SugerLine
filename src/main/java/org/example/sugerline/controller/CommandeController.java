package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.example.sugerline.service.CommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<CommandeResponseDTO>> getAllCommandes(){
        List<CommandeResponseDTO> commandes =  commandeService.getAllCommande();
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
}
