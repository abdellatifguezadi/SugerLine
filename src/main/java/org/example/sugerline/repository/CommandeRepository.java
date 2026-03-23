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
    

    Long countByUtilisateur(Utilisateur utilisateur);
    
    Long countByUtilisateurAndStatut(Utilisateur utilisateur, StatutCommande statut);

    


    @Query("SELECT SUM(cl.quantite * prod.prixVente) FROM CommandeLine cl " +
           "JOIN cl.produit prod " +
           "JOIN cl.commande c " +
           "WHERE c.statut = :statut")
    Double sumRevenueByStatut(@Param("statut") StatutCommande statut);

    @Query("SELECT SUM(cl.quantite * prod.prixVente) FROM CommandeLine cl " +
           "JOIN cl.produit prod " +
           "JOIN cl.commande c " +
           "WHERE c.statut = :statut AND MONTH(c.date) = :mois AND YEAR(c.date) = :annee")
    Double sumRevenueByMoisAndAnnee(@Param("statut") StatutCommande statut, @Param("mois") Integer mois, @Param("annee") Integer annee);
}
