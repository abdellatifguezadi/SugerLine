package org.example.sugerline.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {

    @NotBlank(message = "Le contenu du message est obligatoire")
    @Size(min = 1, max = 5000, message = "Le contenu doit contenir entre 1 et 5000 caractères")
    private String contenu;

    @NotBlank(message = "L'expéditeur est obligatoire")
    private String expediteur;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;
}

