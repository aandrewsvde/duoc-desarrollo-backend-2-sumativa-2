package com.minimarket.controller;

import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolaMundoController.class)
@DisplayName("Tests del controlador HolaMundo (endpoint publico)")
class HolaMundoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser
    @DisplayName("GET /public/hola debe retornar 200 con mensaje de saludo")
    void testHolaMundo_retorna200() throws Exception {
        mockMvc.perform(get("/public/hola"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Hola Mundo!"));
    }
}
