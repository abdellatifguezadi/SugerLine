package org.example.sugerline.repository;

import org.example.sugerline.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande , Long> {
}
