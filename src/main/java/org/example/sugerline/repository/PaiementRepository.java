package org.example.sugerline.repository;

import org.example.sugerline.entity.Paiement;
import org.example.sugerline.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaiementRepository extends JpaRepository<Paiement, Long>, JpaSpecificationExecutor<Paiement> {
    boolean existsByCommandeId(Long commandeId);
    boolean existsByCommandeIdAndStatutIn(Long commandeId, java.util.List<StatutPaiement> statuts);
}
