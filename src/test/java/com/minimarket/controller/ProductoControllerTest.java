package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
@DisplayName("Tests del controlador de Productos")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche Entera");
        producto.setPrecio(1500.0);
        producto.setStock(50);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/productos debe retornar 200 con lista de productos")
    void testListarProductos_retorna200() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());

        verify(productoService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/productos/{id} debe retornar 200 cuando el producto existe")
    void testObtenerProducto_retorna200_cuandoExiste() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/productos/{id} debe retornar 404 cuando el producto no existe")
    void testObtenerProducto_retorna404_cuandoNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/productos debe retornar 200 con el producto guardado")
    void testGuardarProducto_retorna200() throws Exception {
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Leche Entera\",\"precio\":1500.0,\"stock\":50}"))
                .andExpect(status().isOk());

        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/productos/{id} debe retornar 200 cuando el producto existe")
    void testActualizarProducto_retorna200_cuandoExiste() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto);
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Leche Descremada\",\"precio\":1600.0,\"stock\":40}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/productos/{id} debe retornar 404 cuando el producto no existe")
    void testActualizarProducto_retorna404_cuandoNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"X\",\"precio\":100.0,\"stock\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/productos/{id} debe retornar 204 cuando el producto existe")
    void testEliminarProducto_retorna204_cuandoExiste() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto);
        doNothing().when(productoService).deleteById(1L);

        mockMvc.perform(delete("/api/productos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/productos/{id} debe retornar 404 cuando el producto no existe")
    void testEliminarProducto_retorna404_cuandoNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
