package org.example.sugerline.repository;

import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande , Long>, JpaSpecificationExecutor<Commande> {
    
    @Query("SELECT c FROM Commande c WHERE MONTH(c.date) = :mois AND YEAR(c.date) = :annee AND c.statut = :statut")
    List<Commande> findByMoisAndAnneeAndStatut(@Param("mois") Integer mois, @Param("annee") Integer annee, @Param("statut") StatutCommande statut);

    Long countByStatut(StatutCommande statut);
    
    Long countByDateBetween(LocalDate debut, LocalDate fin);
    
    Long countByUtilisateur(Utilisateur utilisateur);
    
    Long countByUtilisateurAndStatut(Utilisateur utilisateur, StatutCommande statut);
    
    Long countByUtilisateurAndDateBetween(Utilisateur utilisateur, LocalDate debut, LocalDate fin);
    
    @Query("SELECT p.nom, SUM(cl.quantite) as total FROM CommandeLine cl " +
           "JOIN cl.produit p " +
           "JOIN cl.commande c " +
           "WHERE c.statut = 'LIVREE' " +
           "GROUP BY p.id, p.nom " +
           "ORDER BY total DESC")
    List<Object[]> findTopProduits();
    
    @Query("SELECT p.nom, SUM(cl.quantite) as total FROM CommandeLine cl " +
           "JOIN cl.produit p " +
           "JOIN cl.commande c " +
           "WHERE c.utilisateur = :utilisateur AND c.statut = 'LIVREE' " +
           "GROUP BY p.id, p.nom " +
           "ORDER BY total DESC")
    List<Object[]> findTopProduitsByUtilisateur(@Param("utilisateur") Utilisateur utilisateur);
}
