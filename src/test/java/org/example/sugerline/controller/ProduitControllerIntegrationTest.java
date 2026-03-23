package org.example.sugerline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sugerline.dto.request.ProduitRequestDTO;
import org.example.sugerline.dto.request.ProduitUpdateDto;
import org.example.sugerline.dto.response.ProduitResponseDTO;
import org.example.sugerline.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProduitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProduitResponseDTO produitResponseDTO;
    private ProduitRequestDTO produitRequestDTO;
    private ProduitUpdateDto produitUpdateDto;

    @BeforeEach
    void setUp() {
        produitResponseDTO = new ProduitResponseDTO();
        produitResponseDTO.setId(1L);
        produitResponseDTO.setNom("Test Produit");

        produitRequestDTO = new ProduitRequestDTO();
        produitRequestDTO.setNom("Test Produit");
        produitRequestDTO.setPrixVente(10.0);
        produitUpdateDto = new ProduitUpdateDto();
        produitUpdateDto.setNom("Updated Produit");
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testCreateProduit() throws Exception {
        when(produitService.createProduit(any(ProduitRequestDTO.class))).thenReturn(produitResponseDTO);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produitRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("Test Produit"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testUpdateProduit() throws Exception {
        when(produitService.updateProduit(eq(1L), any(ProduitUpdateDto.class))).thenReturn(produitResponseDTO);

        mockMvc.perform(put("/api/produits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produitUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void testGetAllProduits() throws Exception {
        Page<ProduitResponseDTO> page = new PageImpl<>(Collections.singletonList(produitResponseDTO), PageRequest.of(0, 10), 1);
        when(produitService.getAllProduits(any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    @WithMockUser
    void testGetProduitById() throws Exception {
        when(produitService.getProduitById(1L)).thenReturn(produitResponseDTO);

        mockMvc.perform(get("/api/produits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testDeleteProduit() throws Exception {
        doNothing().when(produitService).deleteProduit(1L);

        mockMvc.perform(delete("/api/produits/1"))
                .andExpect(status().isNoContent());
    }
}
