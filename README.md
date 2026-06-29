# Minimarket Plus - Backend

Sistema de gestión backend para Minimarket Plus, desarrollado con Spring Boot. Incluye gestión de productos, inventario, ventas, usuarios y carrito de compras, con autenticación mediante Spring Security y una suite completa de pruebas unitarias con JUnit 5 y Mockito.

## Tecnologías utilizadas

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.4.1 | Framework backend |
| Spring Security | 6.x | Autenticación y control de acceso |
| Spring Data JPA | 3.x | Persistencia de datos |
| H2 Database | (runtime) | Base de datos en memoria |
| JUnit 5 | 5.x | Framework de pruebas unitarias |
| Mockito | 5.x | Mocking de dependencias en tests |
| JaCoCo | 0.8.11 | Cobertura de código |
| Maven | 3.x | Gestión de dependencias y build |
| Lombok | latest | Reducción de código boilerplate |

## Estructura del proyecto

```
minimarket/
├── src/
│   ├── main/
│   │   ├── java/com/minimarket/
│   │   │   ├── controller/          # Controladores REST
│   │   │   │   ├── CarritoController.java
│   │   │   │   ├── InventarioController.java
│   │   │   │   ├── ProductoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── VentaController.java
│   │   │   ├── entity/              # Entidades JPA
│   │   │   │   ├── Carrito.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── DetalleVenta.java
│   │   │   │   ├── Inventario.java
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Usuario.java
│   │   │   │   └── Venta.java
│   │   │   ├── repository/          # Repositorios Spring Data
│   │   │   ├── security/            # Configuración de seguridad
│   │   │   │   ├── config/SecurityConfig.java
│   │   │   │   ├── model/CustomUserDetails.java
│   │   │   │   └── util/JwtUtil.java
│   │   │   ├── service/             # Interfaces de servicio
│   │   │   │   └── impl/            # Implementaciones de servicio
│   │   │   └── MinimarketApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/minimarket/
│           ├── service/             # Tests de servicios con Mockito
│           │   ├── CarritoServiceTest.java
│           │   ├── InventarioServiceTest.java
│           │   ├── ProductoServiceTest.java
│           │   ├── UsuarioServiceTest.java
│           │   └── VentaServiceTest.java
│           ├── InventarioTest.java  # Tests de entidad Inventario
│           ├── MinimarketApplicationTests.java
│           ├── ProductoTest.java    # Tests de entidad Producto
│           ├── UsuarioTest.java     # Tests de entidad Usuario
│           └── VentaTest.java      # Tests de entidad Venta
├── pom.xml
└── README.md
```

## Módulos del sistema

- **Productos**: CRUD completo con búsqueda por categoría.
- **Inventario**: Registro de movimientos de entrada y salida de stock.
- **Ventas**: Generación de ventas con detalle de productos adquiridos.
- **Carrito**: Gestión del carrito de compras por usuario.
- **Usuarios**: Registro y autenticación con roles (ADMIN, CAJERO, CLIENTE).
- **Seguridad**: Autenticación mediante Spring Security con BCrypt.

## Cómo ejecutar la aplicación

### Requisitos previos
- Java 17+
- Maven 3.6+

### Ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

La consola H2 se encuentra en: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)

## Cómo ejecutar las pruebas unitarias

### Ejecutar todos los tests

```bash
./mvnw test
```

### Ejecutar tests y generar reporte de cobertura (JaCoCo)

```bash
./mvnw verify
```

El reporte HTML de cobertura se genera en:
```
target/site/jacoco/index.html
```

## Suite de pruebas

### Tests de entidades (sin dependencias externas)

| Clase de Test | Tests | Descripción |
|---|---|---|
| `UsuarioTest` | 3 | Creación, comparación y asignación de roles |
| `ProductoTest` | 4 | Creación, actualización de precio/stock, categoría |
| `InventarioTest` | 4 | Movimientos de entrada/salida, asociación a producto |
| `VentaTest` | 3 | Creación, detalles múltiples, verificación de rol cajero |

### Tests de servicios (con Mockito)

| Clase de Test | Tests | Descripción |
|---|---|---|
| `ProductoServiceTest` | 8 | CRUD completo + búsqueda por categoría (éxito y error) |
| `InventarioServiceTest` | 9 | CRUD + registro de entradas/salidas (éxito y error) |
| `VentaServiceTest` | 7 | CRUD + búsqueda por usuario (éxito y error) |
| `UsuarioServiceTest` | 8 | CRUD + búsqueda por username/autenticación (éxito y error) |
| `CarritoServiceTest` | 8 | CRUD + búsqueda por usuario (éxito y error) |

**Total: 45 pruebas unitarias**

## Cobertura de código

Se utiliza JaCoCo para medir la cobertura de las pruebas. Los principales servicios cuentan con cobertura en:

- Escenarios de éxito (happy path)
- Escenarios de error (entidad no encontrada, lista vacía)
- Operaciones CRUD completas

## Autor

- Nombre: Agustín Andrews
- Asignatura: Desarrollo Backend II
- Institución: Duoc UC
- Semana: 6 - Evaluación Sumativa
