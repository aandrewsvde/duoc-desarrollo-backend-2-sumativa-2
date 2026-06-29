package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Usuarios")
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario admin;
    private Usuario cliente;

    @BeforeEach
    void setUp() {
        admin = new Usuario();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("$2a$10$hashedPasswordAdmin");
        admin.setRoles(Set.of(new Rol("ADMIN")));

        cliente = new Usuario();
        cliente.setId(2L);
        cliente.setUsername("cliente01");
        cliente.setPassword("$2a$10$hashedPasswordCliente");
        cliente.setRoles(Set.of(new Rol("CLIENTE")));
    }

    // ===== Escenarios de ÉXITO =====

    @Test
    @DisplayName("findAll debe retornar todos los usuarios registrados")
    void testFindAll_retornaUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(admin, cliente));

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById debe retornar el usuario cuando existe")
    void testFindById_retornaUsuario_cuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<Usuario> resultado = usuarioService.findById(1L);

        assertTrue(resultado.isPresent(), "El Optional debe contener un usuario");
        assertEquals("admin", resultado.get().getUsername());
        assertTrue(resultado.get().getRoles().stream()
                .anyMatch(r -> r.getNombre().equals("ADMIN")));
    }

    @Test
    @DisplayName("findByUsername debe retornar el usuario cuando el username existe")
    void testFindByUsername_retornaUsuario_cuandoExiste() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        Optional<Usuario> resultado = usuarioService.findByUsername("admin");

        assertTrue(resultado.isPresent(), "El Optional debe contener el usuario");
        assertEquals(1L, resultado.get().getId());
        assertEquals("admin", resultado.get().getUsername());
    }

    @Test
    @DisplayName("save debe persistir y retornar el usuario guardado")
    void testSave_guardaYRetornaUsuario() {
        when(usuarioRepository.save(admin)).thenReturn(admin);

        Usuario resultado = usuarioService.save(admin);

        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        assertEquals(1, resultado.getRoles().size());
        verify(usuarioRepository, times(1)).save(admin);
    }

    @Test
    @DisplayName("deleteById debe invocar la eliminación en el repositorio")
    void testDeleteById_eliminaUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // ===== Escenarios de ERROR / BORDE =====

    @Test
    @DisplayName("findAll debe retornar lista vacía cuando no hay usuarios")
    void testFindAll_retornaListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("findById debe retornar Optional vacío cuando el usuario no existe")
    void testFindById_retornaOptionalVacio_cuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findById(99L);

        assertFalse(resultado.isPresent(), "El Optional debe estar vacío para un ID inexistente");
    }

    @Test
    @DisplayName("findByUsername debe retornar Optional vacío para credenciales inválidas")
    void testFindByUsername_retornaOptionalVacio_cuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("usuarioInexistente")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findByUsername("usuarioInexistente");

        assertFalse(resultado.isPresent(),
                "Debe retornar Optional vacío para un usuario que no existe (intento de autenticación inválido)");
    }
}
