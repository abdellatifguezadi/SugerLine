package org.example.sugerline.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint public accessible à tous");
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> userEndpoint(Principal principal) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Endpoint accessible à tous les utilisateurs authentifiés");
        response.put("username", principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint réservé aux ADMINISTRATEURS");
    }

    @GetMapping("/caissier")
    @PreAuthorize("hasRole('CAISSIER')")
    public ResponseEntity<String> caissierEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint réservé aux CAISSIERS");
    }

    @GetMapping("/magasin")
    @PreAuthorize("hasRole('MAGASIN')")
    public ResponseEntity<String> magasinEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint réservé au personnel MAGASIN");
    }

    @GetMapping("/livreur")
    @PreAuthorize("hasRole('LIVREUR')")
    public ResponseEntity<String> livreurEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint réservé aux LIVREURS");
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> clientEndpoint() {
        return ResponseEntity.ok("Ceci est un endpoint réservé aux CLIENTS");
    }

    @GetMapping("/admin-or-caissier")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'CAISSIER')")
    public ResponseEntity<String> adminOrCaissierEndpoint() {
        return ResponseEntity.ok("Endpoint accessible aux ADMINISTRATEURS et CAISSIERS");
    }
}

