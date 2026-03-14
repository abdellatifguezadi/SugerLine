package org.example.sugerline.repository;

import org.example.sugerline.entity.Paiement;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaiementRepository extends JpaRepository<Paiement, Long>, JpaSpecificationExecutor<Paiement> {
    boolean existsByCommandeId(Long commandeId);
    boolean existsByCommandeIdAndStatutIn(Long commandeId, java.util.List<StatutPaiement> statuts);
    
    Long countByStatut(StatutPaiement statut);
    
    Long countByUtilisateur(Utilisateur utilisateur);
    
    Long countByUtilisateurAndStatut(Utilisateur utilisateur, StatutPaiement statut);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.statut = :statut")
    Double sumMontantByStatut(@Param("statut") StatutPaiement statut);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.statut = :statut AND p.date BETWEEN :debut AND :fin")
    Double sumMontantByStatutAndDateBetween(@Param("statut") StatutPaiement statut, 
                                             @Param("debut") LocalDateTime debut, 
                                             @Param("fin") LocalDateTime fin);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.utilisateur = :utilisateur AND p.statut = :statut")
    Double sumMontantByUtilisateurAndStatut(@Param("utilisateur") Utilisateur utilisateur, 
                                             @Param("statut") StatutPaiement statut);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.utilisateur = :utilisateur AND p.statut = :statut AND p.date BETWEEN :debut AND :fin")
    Double sumMontantByUtilisateurAndStatutAndDateBetween(@Param("utilisateur") Utilisateur utilisateur, 
                                                            @Param("statut") StatutPaiement statut,
                                                            @Param("debut") LocalDateTime debut, 
                                                            @Param("fin") LocalDateTime fin);
}
