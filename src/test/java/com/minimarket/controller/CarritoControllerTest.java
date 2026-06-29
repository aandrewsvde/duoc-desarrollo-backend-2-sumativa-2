package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.CarritoService;
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

@WebMvcTest(CarritoController.class)
@DisplayName("Tests del controlador de Carrito")
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Carrito carrito;

    @BeforeEach
    void setUp() {
        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCantidad(3);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/carrito debe retornar 200 con lista del carrito")
    void testListarCarrito_retorna200() throws Exception {
        when(carritoService.findAll()).thenReturn(List.of(carrito));

        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk());

        verify(carritoService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/carrito/{id} debe retornar 200 cuando existe")
    void testObtenerCarrito_retorna200_cuandoExiste() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito);

        mockMvc.perform(get("/api/carrito/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/carrito/{id} debe retornar 404 cuando no existe")
    void testObtenerCarrito_retorna404_cuandoNoExiste() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/carrito/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/carrito debe agregar producto y retornar 200")
    void testAgregarAlCarrito_retorna200() throws Exception {
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito);

        mockMvc.perform(post("/api/carrito")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":3}"))
                .andExpect(status().isOk());

        verify(carritoService, times(1)).save(any(Carrito.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/carrito/{id} debe retornar 200 cuando existe")
    void testActualizarCarrito_retorna200_cuandoExiste() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito);
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito);

        mockMvc.perform(put("/api/carrito/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":5}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/carrito/{id} debe retornar 404 cuando no existe")
    void testActualizarCarrito_retorna404_cuandoNoExiste() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/carrito/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/carrito/{id} debe retornar 204 cuando existe")
    void testEliminarDeCarrito_retorna204_cuandoExiste() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito);
        doNothing().when(carritoService).deleteById(1L);

        mockMvc.perform(delete("/api/carrito/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/carrito/{id} debe retornar 404 cuando no existe")
    void testEliminarDeCarrito_retorna404_cuandoNoExiste() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/carrito/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
