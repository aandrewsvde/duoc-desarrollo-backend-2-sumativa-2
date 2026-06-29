package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@DisplayName("Tests del controlador de Categorias")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/categorias debe retornar 200 con lista de categorias")
    void testListarCategorias_retorna200() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk());

        verify(categoriaService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/categorias/{id} debe retornar 200 cuando existe")
    void testObtenerCategoria_retorna200_cuandoExiste() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/categorias/{id} debe retornar 404 cuando no existe")
    void testObtenerCategoria_retorna404_cuandoNoExiste() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/categorias debe guardar y retornar 200")
    void testGuardarCategoria_retorna200() throws Exception {
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Bebidas\"}"))
                .andExpect(status().isOk());

        verify(categoriaService, times(1)).save(any(Categoria.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/categorias/{id} debe retornar 200 cuando existe")
    void testActualizarCategoria_retorna200_cuandoExiste() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria);
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/categorias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Lacteos\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/categorias/{id} debe retornar 404 cuando no existe")
    void testActualizarCategoria_retorna404_cuandoNoExiste() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/categorias/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"X\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/categorias/{id} debe retornar 204 cuando existe")
    void testEliminarCategoria_retorna204_cuandoExiste() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria);
        doNothing().when(categoriaService).deleteById(1L);

        mockMvc.perform(delete("/api/categorias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/categorias/{id} debe retornar 404 cuando no existe")
    void testEliminarCategoria_retorna404_cuandoNoExiste() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/categorias/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
