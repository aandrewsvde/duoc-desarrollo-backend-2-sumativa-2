package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.DetalleVentaService;
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

@WebMvcTest(DetalleVentaController.class)
@DisplayName("Tests del controlador de Detalle de Ventas")
class DetalleVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private DetalleVenta detalle;

    @BeforeEach
    void setUp() {
        detalle = new DetalleVenta();
        detalle.setId(1L);
        detalle.setCantidad(2);
        detalle.setPrecio(1500.0);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/detalle-ventas debe retornar 200 con lista de detalles")
    void testListarDetalles_retorna200() throws Exception {
        when(detalleVentaService.findAll()).thenReturn(List.of(detalle));

        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isOk());

        verify(detalleVentaService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/detalle-ventas/{id} debe retornar 200 cuando existe")
    void testObtenerDetalle_retorna200_cuandoExiste() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalle);

        mockMvc.perform(get("/api/detalle-ventas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/detalle-ventas/{id} debe retornar 404 cuando no existe")
    void testObtenerDetalle_retorna404_cuandoNoExiste() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/detalle-ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/detalle-ventas debe guardar y retornar 200")
    void testGuardarDetalle_retorna200() throws Exception {
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalle);

        mockMvc.perform(post("/api/detalle-ventas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":2,\"precio\":1500.0}"))
                .andExpect(status().isOk());

        verify(detalleVentaService, times(1)).save(any(DetalleVenta.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/detalle-ventas/{id} debe retornar 200 cuando existe")
    void testActualizarDetalle_retorna200_cuandoExiste() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalle);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalle);

        mockMvc.perform(put("/api/detalle-ventas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":3,\"precio\":1500.0}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/detalle-ventas/{id} debe retornar 404 cuando no existe")
    void testActualizarDetalle_retorna404_cuandoNoExiste() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/detalle-ventas/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":1,\"precio\":100.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/detalle-ventas/{id} debe retornar 204 cuando existe")
    void testEliminarDetalle_retorna204_cuandoExiste() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalle);
        doNothing().when(detalleVentaService).deleteById(1L);

        mockMvc.perform(delete("/api/detalle-ventas/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/detalle-ventas/{id} debe retornar 404 cuando no existe")
    void testEliminarDetalle_retorna404_cuandoNoExiste() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/detalle-ventas/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
