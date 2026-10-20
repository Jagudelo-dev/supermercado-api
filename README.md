# Supermercado API

API REST para administrar categorías, proveedores, productos, clientes y ventas de un supermercado.
Incluye relaciones entre recursos, validaciones de entrada y control de inventario al vender.

## Stack técnico

- Java 17.
- Spring Boot 4.1.1.
- Maven.
- Spring Web MVC para exponer la API REST.
- Spring Data JPA e Hibernate.
- H2 en memoria durante el desarrollo.
- Jakarta Bean Validation.
- Lombok.

## Requisitos previos

- JDK 17 o superior.
- Maven 3.8 o superior, o el Maven Wrapper incluido (`mvnw`).
- Postman, Thunder Client u otro cliente HTTP para probar la API.
- Git para clonar el repositorio.

## Clonar y ejecutar

Clona el repositorio y entra a la carpeta del proyecto:

```bash
git clone https://github.com/Jagudelo-dev/supermercado-api.git
cd supermercado-api
```

Compila y ejecuta las verificaciones Maven:

```bash
./mvnw clean package
```

En Windows, usa `mvnw.cmd clean package`.

Inicia la aplicación:

```bash
./mvnw spring-boot:run
```

En Windows, usa `mvnw.cmd spring-boot:run`.

La API queda disponible en `http://localhost:8080`.
La base de datos en memoria se crea al iniciar y se mantiene mientras la aplicación está ejecutándose.

## Consola H2

La consola H2 está habilitada durante el desarrollo:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:supermercado`
- Usuario: `sa`
- Contraseña: vacía

La configuración está en `src/main/resources/application.yaml`.
Hibernate usa `ddl-auto: update` para crear o actualizar las tablas al iniciar la aplicación.

## Endpoints

Todos los endpoints utilizan el prefijo indicado en la columna Ruta.
Los identificadores de las rutas deben corresponder a recursos existentes.

### Categorías

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/categorias` | Crear una categoría. |
| GET | `/api/categorias` | Listar las categorías. |
| GET | `/api/categorias/{id}` | Consultar una categoría por ID. |
| PUT | `/api/categorias/{id}` | Actualizar una categoría. |
| DELETE | `/api/categorias/{id}` | Eliminar una categoría sin productos asociados. |

### Proveedores

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/proveedores` | Crear un proveedor. |
| GET | `/api/proveedores` | Listar los proveedores. |
| GET | `/api/proveedores/{id}` | Consultar un proveedor por ID. |
| PUT | `/api/proveedores/{id}` | Actualizar un proveedor. |
| DELETE | `/api/proveedores/{id}` | Eliminar un proveedor sin productos asociados. |

### Productos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/productos` | Crear un producto. |
| GET | `/api/productos` | Listar los productos. |
| GET | `/api/productos/{id}` | Consultar un producto por ID. |
| PUT | `/api/productos/{id}` | Actualizar un producto. |
| DELETE | `/api/productos/{id}` | Eliminar un producto que no tenga ventas asociadas. |
| GET | `/api/productos/categoria/{categoriaId}` | Listar productos de una categoría. |
| GET | `/api/productos/buscar?nombre=agua` | Buscar por coincidencia parcial de nombre, sin distinguir mayúsculas. |
| GET | `/api/productos/stock-bajo?limite=10` | Listar productos con stock menor o igual al límite. |

### Clientes

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/clientes` | Crear un cliente. |
| GET | `/api/clientes` | Listar los clientes. |
| GET | `/api/clientes/{id}` | Consultar un cliente por ID. |
| PUT | `/api/clientes/{id}` | Actualizar un cliente. |
| DELETE | `/api/clientes/{id}` | Eliminar un cliente sin ventas asociadas. |
| GET | `/api/clientes/documento/{documento}` | Buscar un cliente por documento. |

### Ventas

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/ventas` | Crear una venta con uno o más detalles. |
| GET | `/api/ventas` | Listar las ventas. |
| GET | `/api/ventas/{id}` | Consultar una venta por ID. |
| DELETE | `/api/ventas/{id}` | Eliminar una venta y restituir su stock. |
| GET | `/api/ventas/cliente/{clienteId}` | Listar las ventas de un cliente. |
| GET | `/api/ventas/fecha?inicio=2026-01-01T00:00:00&fin=2026-12-31T23:59:59` | Buscar ventas en un rango de fechas. |

Venta no tiene endpoint PUT; la API crea la venta y sus detalles en una sola solicitud.
Los detalles se consultan como parte de la respuesta de venta.

## Ejemplos de uso

Crear una categoría:

```http
POST /api/categorias
Content-Type: application/json
```

```json
{
  "nombre": "Bebidas",
  "descripcion": "Gaseosas, jugos y aguas"
}
```

Crear una venta con sus detalles:

```http
POST /api/ventas
Content-Type: application/json
```

```json
{
  "clienteId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

El servidor consulta el precio del producto y calcula el precio unitario de la venta,
el subtotal y el total. Esos importes no se reciben desde el cliente.
Una respuesta exitosa incluye el ID, la fecha, el total y los detalles calculados.

## Códigos HTTP

| Código | Significado | Uso habitual |
|---|---|---|
| 200 | OK | Consulta o actualización exitosa. |
| 201 | Created | Recurso creado correctamente. |
| 204 | No Content | Eliminación exitosa sin cuerpo de respuesta. |
| 400 | Bad Request | JSON inválido, parámetro mal formado o validación incorrecta. |
| 404 | Not Found | No existe la categoría, proveedor, producto, cliente o venta solicitada. |
| 409 | Conflict | Se incumple una regla de negocio o una restricción de unicidad. |

## Reglas de negocio

- El nombre es obligatorio para categorías, proveedores, productos y clientes.
- El documento de cliente es único.
- El código de barras de producto es único.
- El precio del producto debe ser mayor que cero.
- El stock del producto no puede ser negativo.
- Cada producto requiere una categoría y un proveedor existentes.
- El correo del proveedor, cuando se informa, debe tener formato válido.
- Una venta requiere un cliente existente y al menos un detalle.
- Cada detalle requiere un producto existente y una cantidad positiva.
- El precio unitario de cada detalle conserva el precio vigente al crear la venta.
- El subtotal es la cantidad multiplicada por el precio unitario; el servidor calcula el total.
- La venta no se crea si el stock disponible es insuficiente.
- La validación de stock ocurre antes de descontar las cantidades de la venta.
- Al eliminar una venta, se devuelve al inventario la cantidad registrada en cada detalle.
- No se puede eliminar una categoría o un proveedor que tenga productos asociados.
- No se puede eliminar un producto que aparezca en los detalles de una venta.
- No se puede eliminar un cliente que tenga ventas asociadas.

## Base de datos

El perfil de desarrollo utiliza H2 en memoria y conserva el esquema durante la vida del proceso.
El proyecto incluye `src/main/resources/schema.sql` con instrucciones DDL para MySQL 8+.
Ese script permite crear las tablas en una base MySQL, pero la configuración predeterminada
de `application.yaml` continúa apuntando a H2; para usar MySQL debe configurarse el datasource.

## Colección Postman

Importa `docs/supermercado-api.postman_collection.json` desde Postman mediante **Import**.
La colección agrupa las solicitudes por recurso y define `baseUrl` como `http://localhost:8080`.
Las variables de IDs y documento son valores de ejemplo; actualízalas con los IDs de tu base.
La colección no incluye scripts de test: las peticiones están preparadas para ejecución manual.

## Estructura del proyecto

```text
src/main/java/com/josemiguelagudelo/supermercado_api/
├── controller/   Controladores REST y rutas HTTP.
├── dto/          Objetos de entrada y salida de la API.
├── entity/       Entidades JPA y relaciones de persistencia.
├── exception/    Excepciones de negocio y manejo global de errores.
├── mapper/       Conversión manual entre entidades y DTOs.
├── repository/   Repositorios Spring Data JPA.
└── service/      Casos de uso y reglas de negocio.
src/main/resources/
├── application.yaml   Configuración de Spring y H2.
└── schema.sql         Esquema de base de datos para MySQL 8+.
docs/
└── supermercado-api.postman_collection.json   Solicitudes manuales de Postman.
```

## Autor

José Miguel Agudelo Torres
