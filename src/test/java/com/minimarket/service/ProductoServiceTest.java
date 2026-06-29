package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Productos")
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche Entera");
        producto.setPrecio(1500.0);
        producto.setStock(50);
        producto.setCategoria(categoria);
    }

    // ===== Escenarios de ÉXITO =====

    @Test
    @DisplayName("findAll debe retornar lista con todos los productos")
    void testFindAll_retornaListaDeProductos() {
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Pan de molde");
        producto2.setPrecio(900.0);
        producto2.setStock(30);
        producto2.setCategoria(categoria);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto, producto2));

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado, "La lista no debe ser nula");
        assertEquals(2, resultado.size(), "Debe retornar 2 productos");
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById debe retornar el producto cuando existe")
    void testFindById_retornaProducto_cuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado, "El producto no debe ser nulo");
        assertEquals(1L, resultado.getId());
        assertEquals("Leche Entera", resultado.getNombre());
        assertEquals(1500.0, resultado.getPrecio());
        assertEquals(50, resultado.getStock());
        assertEquals("Bebidas", resultado.getCategoria().getNombre());
    }

    @Test
    @DisplayName("save debe persistir y retornar el producto guardado")
    void testSave_guardaYRetornaProducto() {
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals("Leche Entera", resultado.getNombre());
        assertEquals(1500.0, resultado.getPrecio());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    @DisplayName("deleteById debe invocar la eliminación en el repositorio")
    void testDeleteById_eliminaProducto() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("findByCategoriaId debe retornar productos de la categoría")
    void testFindByCategoriaId_retornaProductosDeLaCategoria() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByCategoriaId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Leche Entera", resultado.get(0).getNombre());
        assertEquals(1L, resultado.get(0).getCategoria().getId());
    }

    // ===== Escenarios de ERROR / BORDE =====

    @Test
    @DisplayName("findAll debe retornar lista vacía cuando no hay productos")
    void testFindAll_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado, "La lista no debe ser nula");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía cuando no hay productos");
    }

    @Test
    @DisplayName("findById debe retornar null cuando el producto no existe")
    void testFindById_retornaNull_cuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.findById(99L);

        assertNull(resultado, "Debe retornar null para un ID inexistente");
    }

    @Test
    @DisplayName("findByCategoriaId debe retornar lista vacía cuando la categoría no tiene productos")
    void testFindByCategoriaId_retornaVacio_cuandoCategoriaNoTieneProductos() {
        when(productoRepository.findByCategoriaId(99L)).thenReturn(Collections.emptyList());

        List<Producto> resultado = productoService.findByCategoriaId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "No debe haber productos para una categoría inexistente");
    }
}
