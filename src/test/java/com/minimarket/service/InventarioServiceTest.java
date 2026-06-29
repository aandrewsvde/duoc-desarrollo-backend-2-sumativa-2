package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Inventario")
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;
    private Inventario movimientoEntrada;
    private Inventario movimientoSalida;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(1200.0);
        producto.setStock(100);

        movimientoEntrada = new Inventario();
        movimientoEntrada.setId(1L);
        movimientoEntrada.setProducto(producto);
        movimientoEntrada.setCantidad(50);
        movimientoEntrada.setTipoMovimiento("Entrada");
        movimientoEntrada.setFechaMovimiento(new Date());

        movimientoSalida = new Inventario();
        movimientoSalida.setId(2L);
        movimientoSalida.setProducto(producto);
        movimientoSalida.setCantidad(20);
        movimientoSalida.setTipoMovimiento("Salida");
        movimientoSalida.setFechaMovimiento(new Date());
    }

    // ===== Escenarios de ÉXITO =====

    @Test
    @DisplayName("findAll debe retornar todos los movimientos de inventario")
    void testFindAll_retornaMovimientos() {
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(movimientoEntrada, movimientoSalida));

        List<Inventario> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById debe retornar el movimiento cuando existe")
    void testFindById_retornaMovimiento_cuandoExiste() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(movimientoEntrada));

        Inventario resultado = inventarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Entrada", resultado.getTipoMovimiento());
        assertEquals(50, resultado.getCantidad());
        assertEquals(producto, resultado.getProducto());
    }

    @Test
    @DisplayName("save debe registrar un movimiento de entrada correctamente")
    void testSave_registraMovimientoEntrada() {
        when(inventarioRepository.save(movimientoEntrada)).thenReturn(movimientoEntrada);

        Inventario resultado = inventarioService.save(movimientoEntrada);

        assertNotNull(resultado);
        assertEquals("Entrada", resultado.getTipoMovimiento());
        assertEquals(50, resultado.getCantidad());
        assertNotNull(resultado.getFechaMovimiento());
        verify(inventarioRepository, times(1)).save(movimientoEntrada);
    }

    @Test
    @DisplayName("save debe registrar un movimiento de salida correctamente")
    void testSave_registraMovimientoSalida() {
        when(inventarioRepository.save(movimientoSalida)).thenReturn(movimientoSalida);

        Inventario resultado = inventarioService.save(movimientoSalida);

        assertNotNull(resultado);
        assertEquals("Salida", resultado.getTipoMovimiento());
        assertEquals(20, resultado.getCantidad());
        verify(inventarioRepository, times(1)).save(movimientoSalida);
    }

    @Test
    @DisplayName("deleteById debe invocar la eliminación en el repositorio")
    void testDeleteById_eliminaMovimiento() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.deleteById(1L);

        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByProductoId debe retornar los movimientos del producto")
    void testFindByProductoId_retornaMovimientosDelProducto() {
        when(inventarioRepository.findByProductoId(1L))
                .thenReturn(Arrays.asList(movimientoEntrada, movimientoSalida));

        List<Inventario> resultado = inventarioService.findByProductoId(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Entrada", resultado.get(0).getTipoMovimiento());
        assertEquals("Salida", resultado.get(1).getTipoMovimiento());
    }

    // ===== Escenarios de ERROR / BORDE =====

    @Test
    @DisplayName("findAll debe retornar lista vacía cuando no hay movimientos")
    void testFindAll_retornaListaVacia() {
        when(inventarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Inventario> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("findById debe retornar null cuando el movimiento no existe")
    void testFindById_retornaNull_cuandoNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        Inventario resultado = inventarioService.findById(99L);

        assertNull(resultado, "Debe retornar null para un movimiento inexistente");
    }

    @Test
    @DisplayName("findByProductoId debe retornar lista vacía cuando el producto no tiene movimientos")
    void testFindByProductoId_retornaVacio_cuandoProductoSinMovimientos() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Collections.emptyList());

        List<Inventario> resultado = inventarioService.findByProductoId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "No debe haber movimientos para un producto inexistente");
    }
}
