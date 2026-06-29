package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.UsuarioService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayName("Tests del controlador de Usuarios")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setPassword("pass");
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/usuarios debe retornar 200 con lista de usuarios")
    void testListarUsuarios_retorna200() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/usuarios/{id} debe retornar 200 cuando existe")
    void testObtenerUsuario_retorna200_cuandoExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/usuarios/{id} debe retornar 404 cuando no existe")
    void testObtenerUsuario_retorna404_cuandoNoExiste() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/usuarios debe guardar y retornar 200")
    void testGuardarUsuario_retorna200() throws Exception {
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/usuarios/{id} debe retornar 200 cuando existe")
    void testActualizarUsuario_retorna200_cuandoExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"adminNuevo\",\"password\":\"newpass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/usuarios/{id} debe retornar 404 cuando no existe")
    void testActualizarUsuario_retorna404_cuandoNoExiste() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"x\",\"password\":\"x\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/usuarios/{id} debe retornar 204 cuando existe")
    void testEliminarUsuario_retorna204_cuandoExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/usuarios/{id} debe retornar 404 cuando no existe")
    void testEliminarUsuario_retorna404_cuandoNoExiste() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/usuarios/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
