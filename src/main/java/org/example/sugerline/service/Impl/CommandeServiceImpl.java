package org.example.sugerline.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.CommandeRequestDTO;
import org.example.sugerline.dto.response.CommandeResponseDTO;
import org.example.sugerline.entity.Commande;
import org.example.sugerline.entity.CommandeLine;
import org.example.sugerline.entity.Produit;
import org.example.sugerline.entity.Utilisateur;
import org.example.sugerline.exception.InvalidOperationException;
import org.example.sugerline.exception.ResourceNotFoundException;
import org.example.sugerline.mapper.CommandeMapper;
import org.example.sugerline.repository.CommandeRepository;
import org.example.sugerline.repository.ProduitRepository;
import org.example.sugerline.repository.UtilisateurRepository;
import org.example.sugerline.service.CommandeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private double getReductionByRole(String role) {
        return switch (role) {
            case "LIVREUR" -> 10.0;
            case "MAGASIN" -> 15.0;
            case "CAISSIER" -> 5.0;
            default -> 0.0;
        };
    }
}
