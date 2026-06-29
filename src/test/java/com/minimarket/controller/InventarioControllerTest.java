package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@DisplayName("Tests del controlador de Inventario")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(50);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/inventario debe retornar 200 con lista de movimientos")
    void testListarMovimientos_retorna200() throws Exception {
        when(inventarioService.findAll()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk());

        verify(inventarioService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/inventario/{id} debe retornar 200 cuando existe")
    void testObtenerMovimiento_retorna200_cuandoExiste() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario);

        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/inventario/{id} debe retornar 404 cuando no existe")
    void testObtenerMovimiento_retorna404_cuandoNoExiste() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/inventario debe registrar movimiento y retornar 200")
    void testRegistrarMovimiento_retorna200() throws Exception {
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/inventario")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":50,\"tipoMovimiento\":\"Entrada\",\"fechaMovimiento\":1000000}"))
                .andExpect(status().isOk());

        verify(inventarioService, times(1)).save(any(Inventario.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/inventario/{id} debe retornar 200 cuando existe")
    void testActualizarMovimiento_retorna200_cuandoExiste() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario);
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(put("/api/inventario/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":30,\"tipoMovimiento\":\"Salida\",\"fechaMovimiento\":1000000}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/inventario/{id} debe retornar 404 cuando no existe")
    void testActualizarMovimiento_retorna404_cuandoNoExiste() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/inventario/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":10,\"tipoMovimiento\":\"Entrada\",\"fechaMovimiento\":1000000}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/inventario/{id} debe retornar 204 cuando existe")
    void testEliminarMovimiento_retorna204_cuandoExiste() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario);
        doNothing().when(inventarioService).deleteById(1L);

        mockMvc.perform(delete("/api/inventario/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/inventario/{id} debe retornar 404 cuando no existe")
    void testEliminarMovimiento_retorna404_cuandoNoExiste() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/inventario/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
