package com.minimarket;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la entidad Inventario")
public class InventarioTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aceite vegetal");
        producto.setPrecio(2500.0);
        producto.setStock(0);
    }

    @Test
    @DisplayName("Debe registrar un movimiento de entrada correctamente")
    void testRegistrarMovimientoEntrada() {
        Inventario entrada = new Inventario();
        entrada.setId(1L);
        entrada.setProducto(producto);
        entrada.setCantidad(100);
        entrada.setTipoMovimiento("Entrada");
        entrada.setFechaMovimiento(new Date());

        assertNotNull(entrada);
        assertEquals("Entrada", entrada.getTipoMovimiento());
        assertEquals(100, entrada.getCantidad());
        assertEquals(producto, entrada.getProducto());
        assertNotNull(entrada.getFechaMovimiento());
    }

    @Test
    @DisplayName("Debe registrar un movimiento de salida correctamente")
    void testRegistrarMovimientoSalida() {
        Inventario salida = new Inventario();
        salida.setId(2L);
        salida.setProducto(producto);
        salida.setCantidad(15);
        salida.setTipoMovimiento("Salida");
        salida.setFechaMovimiento(new Date());

        assertNotNull(salida);
        assertEquals("Salida", salida.getTipoMovimiento());
        assertEquals(15, salida.getCantidad());
        assertEquals(producto, salida.getProducto());
    }

    @Test
    @DisplayName("Debe asociar el movimiento al producto correcto")
    void testMovimientoAsociadoAProducto() {
        Inventario movimiento = new Inventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(50);
        movimiento.setTipoMovimiento("Entrada");
        movimiento.setFechaMovimiento(new Date());

        assertEquals(1L, movimiento.getProducto().getId());
        assertEquals("Aceite vegetal", movimiento.getProducto().getNombre());
    }

    @Test
    @DisplayName("La fecha del movimiento no debe ser nula al registrar")
    void testFechaMovimientoRegistrada() {
        Date ahora = new Date();
        Inventario movimiento = new Inventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(30);
        movimiento.setTipoMovimiento("Entrada");
        movimiento.setFechaMovimiento(ahora);

        assertNotNull(movimiento.getFechaMovimiento(), "La fecha no debe ser nula");
        assertEquals(ahora, movimiento.getFechaMovimiento());
    }
}
