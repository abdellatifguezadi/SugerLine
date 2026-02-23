package org.example.sugerline.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sugerline.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
}