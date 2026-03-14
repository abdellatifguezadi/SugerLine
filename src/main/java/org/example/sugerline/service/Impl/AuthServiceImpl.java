package org.example.sugerline.service.Impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.Role;
import org.example.sugerline.exception.ResourceNotFoundException;
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
    public AuthResponseDTO createUser(RegisterRequestDTO registerRequest, HttpServletResponse response) {
        if (utilisateurRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceNotFoundException("Username déjà utilisé");
        }

        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceNotFoundException("Email déjà utilisé");
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(registerRequest);
        utilisateur.setMotDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()));

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());
        addTokenToCookie(response, token);

        return utilisateurMapper.toAuthResponseDTO(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getMotDePasse()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(utilisateur.getUsername(), utilisateur.getRole().name());
        addTokenToCookie(response, token);

        return utilisateurMapper.toAuthResponseDTO(utilisateur);
    }

    private void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
