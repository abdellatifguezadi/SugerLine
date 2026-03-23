package org.example.sugerline.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;
import org.example.sugerline.dto.response.UtilisateurResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthService {

    AuthResponseDTO createUser(RegisterRequestDTO registerRequest);

    AuthResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse response);

    AuthResponseDTO getCurrentUser(String username);

    Page<AuthResponseDTO> getAllUsers(Pageable pageable);
}
