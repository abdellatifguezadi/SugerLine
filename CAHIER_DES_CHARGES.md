# Cahier des Charges – Plateforme Web de Gestion de Pâtisserie & Boulangerie – SugarLine

**Réalisé par :** Abdellatif Guezadi  
**Technologies :** Spring Boot, Spring Security, JWT, JPA/Hibernate, Angular  
**Année :** 2025/2026  
**YouCode Nador**

---

## Table des matières

1. [Introduction](#1-introduction)
2. [Objectifs du Projet](#2-objectifs-du-projet)
3. [Acteurs du Système](#3-acteurs-du-système)
4. [Périmètre du Projet](#4-périmètre-du-projet)
5. [Fonctionnalités Détaillées](#5-fonctionnalités-détaillées)
   - 5.1 [Espace Administrateur](#51-espace-administrateur)
   - 5.2 [Espace Livreur](#52-espace-livreur)
   - 5.3 [Espace Magasin Partenaire](#53-espace-magasin-partenaire)
   - 5.4 [Espace Caissier](#54-espace-caissier)
6. [Règles de Gestion](#6-règles-de-gestion)
7. [Architecture Technique](#7-architecture-technique)
8. [Contraintes Techniques](#8-contraintes-techniques)
9. [Calcul du Coût de Revient Mensuel](#9-calcul-du-coût-de-revient-mensuel)
10. [Livrables](#10-livrables)
11. [Évolutions Futures](#11-évolutions-futures)
12. [Conclusion](#12-conclusion)

---

## 1. Introduction

La gestion quotidienne d'une pâtisserie et d'une boulangerie nécessite la coordination de plusieurs acteurs ainsi que le traitement d'un grand nombre de commandes provenant de différentes sources. La gestion manuelle de ces opérations peut entraîner des erreurs, un manque de visibilité et une perte de temps.

Ce projet vise à développer une plateforme web centralisée, appelée **SugarLine**, permettant au propriétaire d'une pâtisserie/boulangerie de gérer efficacement les commandes, la production, les paiements et la communication avec les partenaires.

---

## 2. Objectifs du Projet

**Objectif Général :** Mettre en place une plateforme web sécurisée et fiable pour la gestion globale d'une pâtisserie/boulangerie.

**Objectifs Spécifiques :**
- Centraliser toutes les commandes dans un seul système
- Faciliter le suivi et la validation des commandes
- Automatiser le calcul des quantités à produire
- Gérer les tarifs et les paiements
- Améliorer la communication entre les différents acteurs
- Fournir des statistiques pour aider à la prise de décision

---

## 3. Acteurs du Système

- **Administrateur (Propriétaire)**
- **Livreur**
- **Magasin Partenaire**
- **Caissier**
- **Client ordinaire**

---

## 4. Périmètre du Projet

La plateforme couvre :
- La gestion des commandes multi-sources
- La gestion des produits et ingrédients
- Le suivi des paiements
- La messagerie interne
- La consultation des statistiques

---

## 5. Fonctionnalités Détaillées

### 5.1 Espace Administrateur

- **Authentification sécurisée** (Email/Username + Mot de passe, JWT, rôle ROLE_ADMIN)
- **Gestion des Commandes :** consulter, filtrer (date, source, type, statut), valider ou refuser
- **Gestion de la Production :** calcul automatique des quantités par produit
- **Gestion des Ingrédients :** ajouter, modifier, supprimer, associer aux produits, filtrer par disponibilité
- **Gestion des Tarifs et Paiements :** définir tarifs, calcul montant, historique avec filtres
- **Messagerie Interne :** envoyer/recevoir, filtrer par expéditeur et date
- **Statistiques :** nombre de commandes, produits les plus demandés, chiffre d'affaires

### 5.2 Espace Livreur

- **Authentification sécurisée** (ROLE_LIVREUR)
- Envoyer des commandes, consulter historique, filtrer par date/statut
- Visualiser tarifs et montant à payer
- Envoyer/recevoir messages

### 5.3 Espace Magasin Partenaire

- **Authentification sécurisée** (ROLE_MAGASIN)
- Envoyer commandes, consulter historique, filtrer par date/statut
- Visualiser tarifs et montant à payer
- Envoyer/recevoir messages

### 5.4 Espace Caissier

- **Authentification sécurisée** (ROLE_CAISSIER)
- Enregistrer commandes clients, ajouter produits/quantités, modifier avant validation
- Envoyer commandes à l'administrateur, consulter historique, filtrer par date
- Envoyer/recevoir messages

---

## 6. Règles de Gestion

- Toute commande validée par l'administrateur
- Tarifs définis uniquement par l'administrateur
- Commandes validées non modifiables
- Accès limité à l'espace utilisateur

---

## 7. Architecture Technique

**Backend :**
- Spring Boot
- Spring Security
- JWT
- JPA/Hibernate

**Frontend :**
- Angular avec Guards, Routing, Services HTTP, Interface responsive

**Base de données :** MySQL

---

## 8. Contraintes Techniques

- Sécurité des données
- Performance et scalabilité
- Maintenabilité

---

## 9. Calcul du Coût de Revient Mensuel

### 9.1 Objectif

Permettre à l'administrateur de calculer le bénéfice mensuel en prenant en compte le coût de production des produits et les charges fixes mensuelles.

### 9.2 Gestion des Coûts de Production

#### 9.2.1 Prix de Production par Produit

- Chaque produit possède un **prix de production fixe** (non modifiable)
- Ce prix représente le coût de fabrication unitaire du produit
- Affichage du prix de production dans la fiche produit

#### 9.2.2 Charges Fixes Mensuelles

L'administrateur enregistre les charges fixes du mois :

- **Électricité** : montant mensuel
- **Eau** : montant mensuel
- **Salaires des employés** : total mensuel
- **Loyer** : montant mensuel
- **Autres charges** : montant mensuel (assurances, entretien, etc.)

### 9.3 Calcul du Bénéfice Mensuel

#### Formule de calcul :

```
Coût de production total = Σ (Quantité vendue × Prix de production)
Charges fixes totales = Électricité + Eau + Salaires + Loyer + Autres
Coût total mensuel = Coût de production total + Charges fixes totales
Bénéfice mensuel = Chiffre d'affaires - Coût total mensuel
```

### 9.4 Rapport Mensuel

L'administrateur peut consulter un rapport mensuel comprenant :

- **Chiffre d'affaires** : total des ventes du mois
- **Coût de production** : coût total de fabrication des produits vendus
- **Charges fixes** : détail et total des charges mensuelles
- **Coût total** : production + charges fixes
- **Bénéfice net** : chiffre d'affaires - coût total
- **Taux de rentabilité** : (Bénéfice / Chiffre d'affaires) × 100

### 9.5 Interface Utilisateur

#### 9.5.1 Gestion des Charges Fixes

- Formulaire de saisie des charges mensuelles
- Historique des charges par mois
- Modification possible avant clôture du mois

#### 9.5.2 Rapport Mensuel

- Sélection du mois
- Tableau récapitulatif des coûts et bénéfices
- Graphique d'évolution mensuelle
- Export en PDF

### 9.6 Règles de Gestion

- Le prix de production d'un produit est fixe et ne peut pas être modifié
- Les charges fixes sont saisies une fois par mois par l'administrateur
- Le rapport mensuel est généré automatiquement à partir des données de ventes et des charges
- Seul l'administrateur a accès au module de coût de revient

### 9.7 Données à Stocker

**Table Produit :**
- id, nom, prix_production, prix_vente

**Table Charges_Mensuelles :**
- id, mois, année, electricite, eau, salaires, loyer, autres, total

**Table Rapport_Mensuel :**
- id, mois, année, chiffre_affaires, cout_production, charges_fixes, cout_total, benefice, taux_rentabilite

---

## 10. Livrables

- Code source Backend
- Code source Frontend
- Base de données
- Documentation technique
- Diagrammes UML

---

## 11. Évolutions Futures

- Intégration de fonctionnalités avancées de prévision de production
- Application mobile pour les livreurs et magasins partenaires
- Système de fidélité client
- Intégration avec des systèmes de paiement en ligne
- Module de gestion des stocks en temps réel
- Analyse prédictive des coûts et des ventes

---

## 12. Conclusion

La plateforme **SugarLine** permet d'améliorer l'organisation, la productivité et la visibilité des activités d'une pâtisserie/boulangerie. Avec l'ajout du module de calcul du coût de revient mensuel, elle offre également une vision financière précise permettant d'optimiser la rentabilité. Elle constitue une solution moderne et évolutive adaptée aux besoins du secteur.

---

**Document créé le :** 2025  
**Dernière mise à jour :** 2025  
**Version :** 1.1
