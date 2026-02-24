package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.mapper.UtilisateurMapper;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.security.JwtUtil;
import org.example.sugerline.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurMapper utilisateurMapper;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        if (utilisateurRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username déjà utilisé");
        }

        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(registerRequest);
        utilisateur.setMotDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()));

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        AuthResponseDTO response = utilisateurMapper.toAuthResponseDTO(savedUser);
        response.setToken(jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name()));

        return response;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getMotDePasse()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        AuthResponseDTO response = utilisateurMapper.toAuthResponseDTO(utilisateur);
        response.setToken(jwtUtil.generateToken(utilisateur.getUsername(), utilisateur.getRole().name()));

        return response;
    }
}
