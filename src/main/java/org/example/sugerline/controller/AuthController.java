package org.example.sugerline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            AuthResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Username ou mot de passe incorrect");
        }
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            AuthResponseDTO response = authService.createUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}