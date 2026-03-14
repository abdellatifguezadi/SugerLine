package org.example.sugerline.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;

public interface IAuthService {

    AuthResponseDTO createUser(RegisterRequestDTO registerRequest, HttpServletResponse response);

    AuthResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse response);
}
