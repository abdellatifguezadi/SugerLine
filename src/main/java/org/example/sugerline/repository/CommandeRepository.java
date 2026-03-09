package org.example.sugerline.repository;

import org.example.sugerline.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommandeRepository extends JpaRepository<Commande , Long>, JpaSpecificationExecutor<Commande> {
}
