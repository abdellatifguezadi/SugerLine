package org.example.sugerline.service;

import org.example.sugerline.dto.request.LoginRequestDTO;
import org.example.sugerline.dto.request.RegisterRequestDTO;
import org.example.sugerline.dto.response.AuthResponseDTO;

public interface IAuthService {

    AuthResponseDTO register(RegisterRequestDTO registerRequest);

    AuthResponseDTO login(LoginRequestDTO loginRequest);
}
