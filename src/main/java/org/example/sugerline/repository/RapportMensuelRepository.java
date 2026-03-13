package org.example.sugerline.repository;

import org.example.sugerline.entity.RapportMensuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RapportMensuelRepository extends JpaRepository<RapportMensuel, Long> {
    
    Optional<RapportMensuel> findByMoisAndAnnee(Integer mois, Integer annee);
    
    boolean existsByMoisAndAnnee(Integer mois, Integer annee);
}
