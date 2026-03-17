package org.example.sugerline.repository;

import org.example.sugerline.entity.RapportMensuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RapportMensuelRepository extends JpaRepository<RapportMensuel, Long> {
    
    Optional<RapportMensuel> findByMoisAndAnnee(Integer mois, Integer annee);
    
    boolean existsByMoisAndAnnee(Integer mois, Integer annee);
    
    @Query("SELECT r.benefice FROM RapportMensuel r WHERE r.mois = :mois AND r.annee = :annee")
    Double findBeneficeByMoisAndAnnee(@Param("mois") Integer mois, @Param("annee") Integer annee);

    boolean existsByChargesId(Long chargesId);

    @Query("SELECT SUM(r.chiffreAffaires) FROM RapportMensuel r")
    Double sumAllChiffreAffaires();
}
