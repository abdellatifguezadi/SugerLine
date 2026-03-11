package org.example.sugerline.specification;

import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.Paiement;
import org.example.sugerline.entity.Produit;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.enums.StatutPaiement;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder {

    public static Specification<Produit> produitSpec(String nom, Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nom != null && !nom.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("prixVente"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("prixVente"), maxPrice));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Commande> commandeSpec(String statut, Long utilisateurId, LocalDate from, LocalDate to, Double minTotal, Double maxTotal) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (statut != null && !statut.isBlank()) {
                try {
                    StatutCommande s = StatutCommande.valueOf(statut);
                    predicates.add(cb.equal(root.get("statut"), s));
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (utilisateurId != null) {
                predicates.add(cb.equal(root.get("utilisateur").get("id"), utilisateurId));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            }
            if (minTotal != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("total"), minTotal));
            }
            if (maxTotal != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("total"), maxTotal));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Paiement> paiementSpec(String statut) {
        return paiementSpec(statut, null);
    }

    public static Specification<Paiement> paiementSpec(String statut, Long utilisateurId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (statut != null && !statut.isBlank()) {
                try {
                    StatutPaiement s = StatutPaiement.valueOf(statut.toUpperCase());
                    predicates.add(cb.equal(root.get("statut"), s));
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (utilisateurId != null) {
                predicates.add(cb.equal(
                    root.get("commande").get("utilisateur").get("id"), utilisateurId
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
