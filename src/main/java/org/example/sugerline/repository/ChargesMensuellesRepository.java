package org.example.sugerline.repository;

import org.example.sugerline.entity.ChargesMensuelles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChargesMensuellesRepository extends JpaRepository<ChargesMensuelles, Long>, JpaSpecificationExecutor<ChargesMensuelles> {
    boolean existsByMoisAndAnnee(Integer mois, Integer annee);
    Optional<ChargesMensuelles> findByMoisAndAnnee(Integer mois, Integer annee);
    
    @Query("SELECT SUM(c.total) FROM ChargesMensuelles c")
    Double sumAllCharges();
    

}

