package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.security.model.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de CustomUserDetails (modelo de seguridad)")
public class CustomUserDetailsTest {

    private CustomUserDetails userDetails;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        Rol adminRol = new Rol("ADMIN");
        Rol cajeroRol = new Rol("CAJERO");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setPassword("$2a$10$hashedPassword");
        usuario.setRoles(Set.of(adminRol, cajeroRol));

        userDetails = new CustomUserDetails(usuario);
    }

    @Test
    @DisplayName("getAuthorities debe mapear los roles del usuario como GrantedAuthority")
    void testGetAuthorities_mapea_rolesComoAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities, "Las authorities no deben ser nulas");
        assertEquals(2, authorities.size(), "Debe haber 2 authorities");
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("CAJERO")));
    }

    @Test
    @DisplayName("getUsername debe retornar el username del usuario")
    void testGetUsername_retorna_usernameDelUsuario() {
        assertEquals("admin", userDetails.getUsername());
    }

    @Test
    @DisplayName("getPassword debe retornar la contraseña encriptada del usuario")
    void testGetPassword_retorna_passwordDelUsuario() {
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
    }

    @Test
    @DisplayName("isAccountNonExpired debe retornar true")
    void testIsAccountNonExpired_retornaTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked debe retornar true")
    void testIsAccountNonLocked_retornaTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired debe retornar true")
    void testIsCredentialsNonExpired_retornaTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled debe retornar true")
    void testIsEnabled_retornaTrue() {
        assertTrue(userDetails.isEnabled());
    }
}
