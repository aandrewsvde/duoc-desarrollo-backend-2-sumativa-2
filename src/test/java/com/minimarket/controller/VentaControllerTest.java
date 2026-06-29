package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.VentaService;
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

@WebMvcTest(VentaController.class)
@DisplayName("Tests del controlador de Ventas")
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(1L);
        venta.setFecha(new Date());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/ventas debe retornar 200 con lista de ventas")
    void testListarVentas_retorna200() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk());

        verify(ventaService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/ventas/{id} debe retornar 200 cuando la venta existe")
    void testObtenerVenta_retorna200_cuandoExiste() throws Exception {
        when(ventaService.findById(1L)).thenReturn(venta);

        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/ventas/{id} debe retornar 404 cuando la venta no existe")
    void testObtenerVenta_retorna404_cuandoNoExiste() throws Exception {
        when(ventaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/ventas debe registrar la venta y retornar 200")
    void testGuardarVenta_retorna200() throws Exception {
        when(ventaService.save(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fecha\":1000000}"))
                .andExpect(status().isOk());

        verify(ventaService, times(1)).save(any(Venta.class));
    }
}
