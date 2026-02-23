package org.example.sugerline.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sugerline.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Username est obligatoire")
    private String username;

    @NotBlank(message = "Email est obligatoire")
    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    private String motDePasse;

    @NotBlank(message = "Full name est obligatoire")
    private String fullName;

    @NotNull(message = "Role est obligatoire")
    private Role role;
}

