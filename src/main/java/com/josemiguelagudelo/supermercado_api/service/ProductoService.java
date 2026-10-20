package com.josemiguelagudelo.supermercado_api.service;

import com.josemiguelagudelo.supermercado_api.dto.ProductoRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ProductoResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Categoria;
import com.josemiguelagudelo.supermercado_api.entity.Producto;
import com.josemiguelagudelo.supermercado_api.entity.Proveedor;
import com.josemiguelagudelo.supermercado_api.exception.RecursoNoEncontradoException;
import com.josemiguelagudelo.supermercado_api.exception.ReglaNegocioException;
import com.josemiguelagudelo.supermercado_api.mapper.ProductoMapper;
import com.josemiguelagudelo.supermercado_api.repository.CategoriaRepository;
import com.josemiguelagudelo.supermercado_api.repository.DetalleVentaRepository;
import com.josemiguelagudelo.supermercado_api.repository.ProductoRepository;
import com.josemiguelagudelo.supermercado_api.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoMapper productoMapper;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        return productoMapper.toResponse(obtenerProducto(id));
    }

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        if (productoRepository.existsByCodigoBarras(dto.getCodigoBarras())) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el código de barras: " + dto.getCodigoBarras());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + dto.getCategoriaId()));
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Proveedor no encontrado con id: " + dto.getProveedorId()));

        Producto entidad = new Producto();
        productoMapper.updateEntityFromRequest(dto, entidad);
        entidad.setCategoria(categoria);
        entidad.setProveedor(proveedor);
        return productoMapper.toResponse(productoRepository.save(entidad));
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto entidad = obtenerProducto(id);
        if (productoRepository.existsByCodigoBarrasAndIdNot(dto.getCodigoBarras(), id)) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el código de barras: " + dto.getCodigoBarras());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + dto.getCategoriaId()));
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Proveedor no encontrado con id: " + dto.getProveedorId()));

        productoMapper.updateEntityFromRequest(dto, entidad);
        entidad.setCategoria(categoria);
        entidad.setProveedor(proveedor);
        return productoMapper.toResponse(productoRepository.save(entidad));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerProducto(id);
        if (detalleVentaRepository.existsByProducto_Id(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar: el producto tiene ventas asociadas");
        }
        productoRepository.delete(producto);
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }
}
