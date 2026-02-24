package org.example.sugerline.repository;

import org.example.sugerline.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient , Long> {

}
