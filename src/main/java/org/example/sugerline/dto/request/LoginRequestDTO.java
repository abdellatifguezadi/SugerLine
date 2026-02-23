package org.example.sugerline.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username est obligatoire")
    private String username;

    @NotBlank(message = "Mot de passe est obligatoire")
    private String motDePasse;
}

