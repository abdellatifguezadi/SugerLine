package org.example.sugerline;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.Role;
import org.example.sugerline.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class SugerLineApplication implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SugerLineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!utilisateurRepository.existsByUsername("admin")) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setEmail("admin@sugerline.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrateur");
            admin.setRole(Role.ADMINISTRATEUR);
            utilisateurRepository.save(admin);
            System.out.println("Admin hardcodé créé: username=admin, password=admin123");
        }
    }
}
