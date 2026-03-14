package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.PaiementRequestDTO;
import org.example.sugerline.dto.response.PaiementResponseDTO;
import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.Paiement;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.enums.StatutPaiement;
import org.example.sugerline.exception.InvalidOperationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.PaiementMapper;
import org.example.sugerline.repository.CommandeRepository;
import org.example.sugerline.repository.PaiementRepository;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.service.PaiementService;
import org.example.sugerline.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementMapper paiementMapper;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    @Transactional
    public PaiementResponseDTO createPaiement(PaiementRequestDTO dto) {
        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + dto.getCommandeId()));

        if (commande.getStatut() == StatutCommande.ANNULEE) {
            throw new InvalidOperationException("Impossible de créer un paiement pour une commande annulée");
        }

        if (commande.getStatut() != StatutCommande.LIVREE) {
            throw new InvalidOperationException("Impossible de créer un paiement : la commande doit être LIVRÉE avant le paiement. Statut actuel: " + commande.getStatut());
        }

        if (paiementRepository.existsByCommandeIdAndStatutIn(
                dto.getCommandeId(),
                List.of(StatutPaiement.EN_ATTENTE, StatutPaiement.ACCEPTE))) {
            throw new InvalidOperationException(
                "Un paiement EN_ATTENTE ou ACCEPTE existe déjà pour la commande ID: " + dto.getCommandeId()
            );
        }

        if (Double.compare(dto.getMontant(), commande.getTotal()) != 0) {
            throw new InvalidOperationException(
                    "Le montant du paiement (" + dto.getMontant() + ") doit être exactement égal au total de la commande (" + commande.getTotal() + ")"
            );
        }

        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setCommande(commande);
        paiement.setUtilisateur(commande.getUtilisateur());

        Paiement saved = paiementRepository.save(paiement);
        return paiementMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public PaiementResponseDTO accepterPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + id));

        if (paiement.getStatut() != StatutPaiement.EN_ATTENTE) {
            throw new InvalidOperationException(
                    "Impossible d'accepter un paiement qui n'est pas en attente. Statut actuel: " + paiement.getStatut()
            );
        }

        paiement.setStatut(StatutPaiement.ACCEPTE);

        Commande commande = paiement.getCommande();
        commande.setStatut(StatutCommande.LIVREE);
        commandeRepository.save(commande);

        Paiement saved = paiementRepository.save(paiement);
        return paiementMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public PaiementResponseDTO annulerPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + id));

        if (paiement.getStatut() == StatutPaiement.ACCEPTE) {
            throw new InvalidOperationException(
                    "Impossible d'annuler un paiement déjà accepté. Statut actuel: " + paiement.getStatut()
            );
        }

        if (paiement.getStatut() == StatutPaiement.ANNULE) {
            throw new InvalidOperationException("Ce paiement est déjà annulé");
        }

        paiement.setStatut(StatutPaiement.ANNULE);

        Paiement saved = paiementRepository.save(paiement);
        return paiementMapper.toResponseDTO(saved);
    }

    @Override
    public PaiementResponseDTO getPaiementById(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + id));

        Utilisateur currentUser = getCurrentUser();
        boolean isAdmin = "ADMINISTRATEUR".equals(currentUser.getRole().name());
        boolean isOwner = paiement.getCommande().getUtilisateur().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResourceNotFoundException("Paiement non trouvé avec l'ID: " + id);
        }

        return paiementMapper.toResponseDTO(paiement);
    }

    @Override
    public Page<PaiementResponseDTO> getAllPaiements(String statut, Pageable pageable) {
        Specification<Paiement> spec = SpecificationBuilder.paiementSpec(statut);
        return paiementRepository.findAll(spec, pageable).map(paiementMapper::toResponseDTO);
    }

    @Override
    public Page<PaiementResponseDTO> getMyPaiements(String statut, Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur currentUser = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Specification<Paiement> spec = SpecificationBuilder.paiementSpec(statut, currentUser.getId());
        return paiementRepository.findAll(spec, pageable).map(paiementMapper::toResponseDTO);
    }

    private Utilisateur getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}
