package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Ventas")
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta venta;
    private Usuario cajero;
    private Producto producto;
    private DetalleVenta detalle;

    @BeforeEach
    void setUp() {
        cajero = new Usuario();
        cajero.setId(1L);
        cajero.setUsername("cajero01");
        cajero.setPassword("pass123");
        cajero.setRoles(Set.of(new Rol("CAJERO")));

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Jugo de naranja");
        producto.setPrecio(800.0);
        producto.setStock(20);

        detalle = new DetalleVenta();
        detalle.setId(1L);
        detalle.setProducto(producto);
        detalle.setCantidad(3);
        detalle.setPrecio(800.0);

        venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(cajero);
        venta.setFecha(new Date());
        venta.setDetalles(Arrays.asList(detalle));
        detalle.setVenta(venta);
    }

    // ===== Escenarios de ÉXITO =====

    @Test
    @DisplayName("findAll debe retornar todas las ventas")
    void testFindAll_retornaVentas() {
        Venta venta2 = new Venta();
        venta2.setId(2L);
        venta2.setUsuario(cajero);
        venta2.setFecha(new Date());
        venta2.setDetalles(Collections.emptyList());

        when(ventaRepository.findAll()).thenReturn(Arrays.asList(venta, venta2));

        List<Venta> resultado = ventaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById debe retornar la venta cuando existe")
    void testFindById_retornaVenta_cuandoExiste() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("cajero01", resultado.getUsuario().getUsername());
        assertNotNull(resultado.getFecha());
        assertEquals(1, resultado.getDetalles().size());
    }

    @Test
    @DisplayName("save debe registrar la venta con sus detalles")
    void testSave_registraVentaConDetalles() {
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.save(venta);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertNotNull(resultado.getDetalles());
        assertFalse(resultado.getDetalles().isEmpty(), "La venta debe tener al menos un detalle");
        assertEquals(800.0, resultado.getDetalles().get(0).getPrecio());
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    @DisplayName("findByUsuarioId debe retornar las ventas del cajero")
    void testFindByUsuarioId_retornaVentasDelCajero() {
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(venta));

        List<Venta> resultado = ventaService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("cajero01", resultado.get(0).getUsuario().getUsername());
        assertTrue(resultado.get(0).getUsuario().getRoles()
                .stream().anyMatch(r -> r.getNombre().equals("CAJERO")));
    }

    // ===== Escenarios de ERROR / BORDE =====

    @Test
    @DisplayName("findAll debe retornar lista vacía cuando no hay ventas")
    void testFindAll_retornaListaVacia() {
        when(ventaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Venta> resultado = ventaService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("findById debe retornar null cuando la venta no existe")
    void testFindById_retornaNull_cuandoNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Venta resultado = ventaService.findById(99L);

        assertNull(resultado, "Debe retornar null para una venta inexistente");
    }

    @Test
    @DisplayName("findByUsuarioId debe retornar lista vacía si el usuario no tiene ventas")
    void testFindByUsuarioId_retornaVacio_cuandoUsuarioSinVentas() {
        when(ventaRepository.findByUsuarioId(99L)).thenReturn(Collections.emptyList());

        List<Venta> resultado = ventaService.findByUsuarioId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Usuario sin ventas debe retornar lista vacía");
    }
}
