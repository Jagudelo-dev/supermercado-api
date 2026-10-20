-- ============================================================
-- Script de creación del esquema para MySQL 8+
-- Proyecto: API REST Supermercado
-- Uso: mysql -u root -p < src/main/resources/schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS supermercado
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE supermercado;

-- Tabla: categoria
-- Almacena las categorías de productos del supermercado.
CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: proveedor
-- Almacena los proveedores que suministran productos.
CREATE TABLE proveedor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: cliente
-- Almacena los clientes identificados por un documento único.
CREATE TABLE cliente (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    telefono VARCHAR(20),
    PRIMARY KEY (id),
    CONSTRAINT uk_cliente_documento UNIQUE (documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: producto
-- Almacena productos y sus relaciones obligatorias con categoría y proveedor.
CREATE TABLE producto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    codigo_barras VARCHAR(50) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL,
    categoria_id BIGINT NOT NULL,
    proveedor_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_producto_codigo_barras UNIQUE (codigo_barras),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria (id) ON DELETE RESTRICT,
    CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id)
        REFERENCES proveedor (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: venta
-- Registra las ventas, su fecha, total calculado y cliente asociado.
CREATE TABLE venta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    fecha DATETIME NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id)
        REFERENCES cliente (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: detalle_venta
-- Almacena los productos, cantidades y precios aplicados en cada venta.
CREATE TABLE detalle_venta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id)
        REFERENCES venta (id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id)
        REFERENCES producto (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
