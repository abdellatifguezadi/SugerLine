package org.example.sugerline.service.Impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.exception.AuthenticationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.UtilisateurMapper;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.security.JwtUtil;
import org.example.sugerline.service.IAuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public AuthResponseDTO createUser(RegisterRequestDTO registerRequest) {
        if (utilisateurRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceNotFoundException("Username déjà utilisé");
        }

        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceNotFoundException("Email déjà utilisé");
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(registerRequest);
        utilisateur.setMotDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()));

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toAuthResponseDTO(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getMotDePasse()
                    )
            );
        } catch (Exception e) {
            throw new AuthenticationException("Username ou mot de passe incorrect");
        }

        Utilisateur utilisateur = utilisateurRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException("Username ou mot de passe incorrect"));

        String token = jwtUtil.generateToken(utilisateur.getUsername(), utilisateur.getRole().name());
        addTokenToCookie(response, token);

        return utilisateurMapper.toAuthResponseDTO(utilisateur);
    }

    private void addTokenToCookie(HttpServletResponse response, String token) {
        String cookieValue = String.format("token=%s; Path=/; HttpOnly; Max-Age=%d; SameSite=Lax",
                token, 24 * 60 * 60);
        response.setHeader("Set-Cookie", cookieValue);
    }

    @Override
    public AuthResponseDTO getCurrentUser(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        return utilisateurMapper.toAuthResponseDTO(utilisateur);
    }

    @Override
    public Page<AuthResponseDTO> getAllUsers(Pageable pageable) {
        Page<Utilisateur> users = utilisateurRepository.findAll(pageable);
        return users.map(utilisateurMapper::toAuthResponseDTO);
    }
}
