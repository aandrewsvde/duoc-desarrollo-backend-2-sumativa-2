package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
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
@DisplayName("Tests del servicio de Carrito de Compras")
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carrito;
    private Usuario cliente;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setId(1L);
        cliente.setUsername("cliente01");
        cliente.setPassword("pass");
        cliente.setRoles(Set.of(new Rol("CLIENTE")));

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Agua mineral");
        producto.setPrecio(500.0);
        producto.setStock(100);

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setUsuario(cliente);
        carrito.setProducto(producto);
        carrito.setCantidad(2);
    }

    // ===== Escenarios de ÉXITO =====

    @Test
    @DisplayName("findAll debe retornar todos los items del carrito")
    void testFindAll_retornaCarritos() {
        Carrito carrito2 = new Carrito();
        carrito2.setId(2L);
        carrito2.setUsuario(cliente);
        carrito2.setProducto(producto);
        carrito2.setCantidad(5);

        when(carritoRepository.findAll()).thenReturn(Arrays.asList(carrito, carrito2));

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById debe retornar el carrito cuando existe")
    void testFindById_retornaCarrito_cuandoExiste() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("cliente01", resultado.getUsuario().getUsername());
        assertEquals(2, resultado.getCantidad());
        assertEquals("Agua mineral", resultado.getProducto().getNombre());
    }

    @Test
    @DisplayName("save debe agregar producto al carrito correctamente")
    void testSave_agregaProductoAlCarrito() {
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        Carrito resultado = carritoService.save(carrito);

        assertNotNull(resultado);
        assertEquals(2, resultado.getCantidad());
        assertEquals(producto, resultado.getProducto());
        verify(carritoRepository, times(1)).save(carrito);
    }

    @Test
    @DisplayName("deleteById debe eliminar el item del carrito")
    void testDeleteById_eliminaItemDelCarrito() {
        doNothing().when(carritoRepository).deleteById(1L);

        carritoService.deleteById(1L);

        verify(carritoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByUsuarioId debe retornar los items del carrito del cliente")
    void testFindByUsuarioId_retornaCarritoDelCliente() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(carrito));

        List<Carrito> resultado = carritoService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("cliente01", resultado.get(0).getUsuario().getUsername());
        assertEquals(2, resultado.get(0).getCantidad());
    }

    // ===== Escenarios de ERROR / BORDE =====

    @Test
    @DisplayName("findAll debe retornar lista vacía cuando no hay items en el carrito")
    void testFindAll_retornaListaVacia() {
        when(carritoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("findById debe retornar null cuando el item no existe")
    void testFindById_retornaNull_cuandoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        Carrito resultado = carritoService.findById(99L);

        assertNull(resultado, "Debe retornar null para un carrito inexistente");
    }

    @Test
    @DisplayName("findByUsuarioId debe retornar lista vacía si el cliente no tiene items en carrito")
    void testFindByUsuarioId_retornaVacio_cuandoClienteSinCarrito() {
        when(carritoRepository.findByUsuarioId(99L)).thenReturn(Collections.emptyList());

        List<Carrito> resultado = carritoService.findByUsuarioId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Un cliente sin items debe retornar carrito vacío");
    }
}
