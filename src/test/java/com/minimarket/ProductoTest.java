package com.minimarket;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la entidad Producto")
public class ProductoTest {

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Yogurt natural");
        producto.setPrecio(750.0);
        producto.setStock(40);
        producto.setCategoria(categoria);
    }

    @Test
    @DisplayName("Debe crear un producto con todos sus atributos correctamente")
    void testCrearProducto() {
        assertNotNull(producto);
        assertEquals(1L, producto.getId());
        assertEquals("Yogurt natural", producto.getNombre());
        assertEquals(750.0, producto.getPrecio());
        assertEquals(40, producto.getStock());
        assertNotNull(producto.getCategoria());
        assertEquals("Lacteos", producto.getCategoria().getNombre());
    }

    @Test
    @DisplayName("Debe actualizar el precio del producto correctamente")
    void testActualizarPrecioProducto() {
        Double precioOriginal = producto.getPrecio();
        Double nuevoPrecio = 900.0;

        producto.setPrecio(nuevoPrecio);

        assertNotEquals(precioOriginal, producto.getPrecio(), "El precio debe haber cambiado");
        assertEquals(900.0, producto.getPrecio(), "El nuevo precio debe ser 900.0");
    }

    @Test
    @DisplayName("Debe actualizar el stock del producto correctamente")
    void testActualizarStockProducto() {
        Integer stockInicial = producto.getStock();
        Integer cantidadVendida = 10;
        Integer stockEsperado = stockInicial - cantidadVendida;

        producto.setStock(producto.getStock() - cantidadVendida);

        assertEquals(stockEsperado, producto.getStock(), "El stock debe reducirse tras una venta");
        assertEquals(30, producto.getStock());
    }

    @Test
    @DisplayName("Debe asociar correctamente el producto a su categoría")
    void testProductoAsociadoACategoria() {
        Categoria otraCategoria = new Categoria();
        otraCategoria.setId(2L);
        otraCategoria.setNombre("Panaderia");

        producto.setCategoria(otraCategoria);

        assertEquals(2L, producto.getCategoria().getId());
        assertEquals("Panaderia", producto.getCategoria().getNombre());
    }
}
