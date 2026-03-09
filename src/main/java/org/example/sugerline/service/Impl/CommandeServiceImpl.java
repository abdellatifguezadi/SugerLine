package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.request.CommandeUpdateDTO;
import org.example.sugerline.dto.request.CommandeLineRequestDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.CommandeLine;
import org.example.sugerline.entity.Produit;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.enums.StatutCommande;
import org.example.sugerline.exception.InvalidOperationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.CommandeMapper;
import org.example.sugerline.repository.CommandeRepository;
import org.example.sugerline.repository.ProduitRepository;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.service.CommandeService;
import org.example.sugerline.specification.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CommandeMapper commandeMapper;

    @Override
    @Transactional
    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur currentUser = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Utilisateur utilisateur;
        
        if (commandeRequestDTO.getUtilisateurId() != null && "ADMINISTRATEUR".equals(currentUser.getRole().name())) {
            utilisateur = utilisateurRepository.findById(commandeRequestDTO.getUtilisateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + commandeRequestDTO.getUtilisateurId()));
            
            if ("ADMINISTRATEUR".equals(utilisateur.getRole().name())) {
                throw new InvalidOperationException("Impossible de créer une commande pour un administrateur");
            }
        } else {
            utilisateur = currentUser;
        }

        Commande commande = commandeMapper.toEntity(commandeRequestDTO);
        commande.setUtilisateur(utilisateur);

        double montantTotal = 0.0;

        for (CommandeLine commandeLine : commande.getCommandeLines()) {
            Produit produit = produitRepository.findById(commandeLine.getProduit().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID: " + commandeLine.getProduit().getId()));

            commandeLine.setProduit(produit);
            commandeLine.setCommande(commande);
            
            double lineTotal = produit.getPrixVente() * commandeLine.getQuantite();
            commandeLine.setTotal(lineTotal);
            montantTotal += lineTotal;
        }

        double pourcentageReduction = getReductionByRole(utilisateur.getRole().name());
        double montantReduction = montantTotal * (pourcentageReduction / 100);
        double totalFinal = montantTotal - montantReduction;

        commande.setMontantAvantReduction(montantTotal);
        commande.setPourcentageReduction(pourcentageReduction);
        commande.setMontantReduction(montantReduction);
        commande.setTotal(totalFinal);

        Commande savedCommande = commandeRepository.save(commande);

        return commandeMapper.toResponseDTO(savedCommande);
    }

    @Override
    public Page<CommandeResponseDTO> getAllCommande(String statut, Long utilisateurId, LocalDate from, LocalDate to, Double minTotal, Double maxTotal, Pageable pageable) {
        Specification<Commande> spec = SpecificationBuilder.commandeSpec(statut, utilisateurId, from, to, minTotal, maxTotal);
        Page<Commande> page = commandeRepository.findAll(spec, pageable);
        return page.map(commandeMapper::toResponseDTO);
    }

    @Override
    public CommandeResponseDTO getCommandeById(Long id) {
        Utilisateur currentUser = getCurrentUser();
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        checkCommandeAccess(currentUser, commande);

        return commandeMapper.toResponseDTO(commande);
    }

    @Override
    public CommandeResponseDTO annulerCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Commande non trouvé avec l'ID: " + id));

        if (commande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new InvalidOperationException("Impossible d'annuler une commande qui n'est pas en attente. Statut actuel: " + commande.getStatut());
        }

        commande.setStatut(StatutCommande.ANNULEE);
        Commande commandeAnnulee = commandeRepository.save(commande);

        return commandeMapper.toResponseDTO(commandeAnnulee);
    }

    @Override
    @Transactional
    public CommandeResponseDTO updateCommande(Long id, CommandeUpdateDTO commandeUpdateDTO) {
        Utilisateur currentUser = getCurrentUser();
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        checkCommandeAccess(currentUser, commande);

        commandeMapper.updateEntityFromDTO(commandeUpdateDTO, commande);

        if (commandeUpdateDTO.getCommandeLines() != null && !commandeUpdateDTO.getCommandeLines().isEmpty()) {
            commande.getCommandeLines().clear();

            double montantTotal = 0.0;

            for (CommandeLineRequestDTO lineDTO : commandeUpdateDTO.getCommandeLines()) {
                Produit produit = produitRepository.findById(lineDTO.getProduitId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID: " + lineDTO.getProduitId()));

                double lineTotal = produit.getPrixVente() * lineDTO.getQuantite();
                montantTotal += lineTotal;

                CommandeLine commandeLine = CommandeLine.builder()
                        .produit(produit)
                        .quantite(lineDTO.getQuantite())
                        .commande(commande)
                        .Total(lineTotal)
                        .build();

                commande.getCommandeLines().add(commandeLine);
            }

            double pourcentageReduction = getReductionByRole(commande.getUtilisateur().getRole().name());
            double montantReduction = montantTotal * (pourcentageReduction / 100);
            double totalFinal = montantTotal - montantReduction;

            commande.setMontantAvantReduction(montantTotal);
            commande.setPourcentageReduction(pourcentageReduction);
            commande.setMontantReduction(montantReduction);
            commande.setTotal(totalFinal);
        }

        Commande updatedCommande = commandeRepository.save(commande);
        return commandeMapper.toResponseDTO(updatedCommande);
    }

    @Override
    public Page<CommandeResponseDTO> getMyCommandes(String statut, LocalDate from, LocalDate to, Double minTotal, Double maxTotal, Pageable pageable) {
        Utilisateur currentUser = getCurrentUser();
        Specification<Commande> spec = SpecificationBuilder.commandeSpec(statut, currentUser.getId(), from, to, minTotal, maxTotal);
        Page<Commande> page = commandeRepository.findAll(spec, pageable);
        return page.map(commandeMapper::toResponseDTO);
    }


    private Utilisateur getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    private void checkCommandeAccess(Utilisateur currentUser, Commande commande) {
        boolean isAdmin = "ADMINISTRATEUR".equals(currentUser.getRole().name());
        boolean isOwner = commande.getUtilisateur().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new InvalidOperationException("Vous n'avez pas l'autorisation d'accéder à cette commande");
        }
    }

    private double getReductionByRole(String role) {
        return switch (role) {
            case "LIVREUR" -> 10.0;
            case "MAGASIN" -> 15.0;
            case "CAISSIER" -> 5.0;
            default -> 0.0;
        };
    }
}
