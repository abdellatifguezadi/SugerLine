package org.example.sugerline.repository;

import org.example.sugerline.entity.CommandeLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeLineRepository extends JpaRepository<CommandeLine , Long> {
    boolean existsByProduitId(Long produitId);
}
