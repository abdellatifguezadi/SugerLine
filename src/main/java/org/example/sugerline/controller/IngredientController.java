package org.example.sugerline.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sugerline.dto.request.IngredientRequestDTO;
import org.example.sugerline.dto.request.IngredientUpdateDto;
import org.example.sugerline.dto.response.IngredientResponseDTO;
import org.example.sugerline.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;


    @PostMapping
    public ResponseEntity<IngredientResponseDTO> createIngredient(@Valid @RequestBody IngredientRequestDTO ingredientRequestDTO) {
        IngredientResponseDTO createdIngredient = ingredientService.createIngredient(ingredientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientUpdateDto ingredientUpdateDto){
        IngredientResponseDTO updatedIngredient = ingredientService.updateIngredient(id , ingredientUpdateDto);
        return ResponseEntity.ok(updatedIngredient);
    }
}
