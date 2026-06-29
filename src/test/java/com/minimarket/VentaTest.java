package com.minimarket;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la entidad Venta")
public class VentaTest {

    private Usuario cajero;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        cajero = new Usuario();
        cajero.setId(1L);
        cajero.setUsername("cajero01");
        cajero.setPassword("pass");
        cajero.setRoles(Set.of(new Rol("CAJERO")));

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Bebida energética");
        producto1.setPrecio(1200.0);
        producto1.setStock(30);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Snack");
        producto2.setPrecio(600.0);
        producto2.setStock(50);
    }

    @Test
    @DisplayName("Debe crear una venta con usuario y fecha correctamente")
    void testCrearVenta() {
        Date fechaVenta = new Date();
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(cajero);
        venta.setFecha(fechaVenta);
        venta.setDetalles(Arrays.asList());

        assertNotNull(venta);
        assertEquals(1L, venta.getId());
        assertEquals("cajero01", venta.getUsuario().getUsername());
        assertEquals(fechaVenta, venta.getFecha());
        assertNotNull(venta.getDetalles());
    }

    @Test
    @DisplayName("Debe asociar correctamente los detalles de la venta")
    void testVentaConMultiplesDetalles() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(cajero);
        venta.setFecha(new Date());

        DetalleVenta detalle1 = new DetalleVenta();
        detalle1.setId(1L);
        detalle1.setVenta(venta);
        detalle1.setProducto(producto1);
        detalle1.setCantidad(2);
        detalle1.setPrecio(1200.0);

        DetalleVenta detalle2 = new DetalleVenta();
        detalle2.setId(2L);
        detalle2.setVenta(venta);
        detalle2.setProducto(producto2);
        detalle2.setCantidad(3);
        detalle2.setPrecio(600.0);

        venta.setDetalles(Arrays.asList(detalle1, detalle2));

        assertEquals(2, venta.getDetalles().size());
        assertEquals("Bebida energética", venta.getDetalles().get(0).getProducto().getNombre());
        assertEquals("Snack", venta.getDetalles().get(1).getProducto().getNombre());
        assertEquals(2, venta.getDetalles().get(0).getCantidad());
        assertEquals(3, venta.getDetalles().get(1).getCantidad());
    }

    @Test
    @DisplayName("Debe verificar que la venta está asociada a un cajero con el rol correcto")
    void testVentaAsociadaACajeroConRolCorrecto() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(cajero);
        venta.setFecha(new Date());
        venta.setDetalles(Arrays.asList());

        assertNotNull(venta.getUsuario());
        assertFalse(venta.getUsuario().getRoles().isEmpty(), "El cajero debe tener roles asignados");
        assertTrue(venta.getUsuario().getRoles().stream()
                .anyMatch(r -> r.getNombre().equals("CAJERO")),
                "El usuario de la venta debe tener el rol CAJERO");
    }
}
