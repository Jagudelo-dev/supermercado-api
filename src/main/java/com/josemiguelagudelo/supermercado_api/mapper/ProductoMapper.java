package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.ProductoRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ProductoResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Producto;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir entre Producto y sus DTOs.
 */
@Component
public class ProductoMapper {

    public ProductoResponseDTO toResponse(Producto entidad) {
        return ProductoResponseDTO.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .descripcion(entidad.getDescripcion())
                .codigoBarras(entidad.getCodigoBarras())
                .precio(entidad.getPrecio())
                .stock(entidad.getStock())
                .categoriaId(entidad.getCategoria().getId())
                .categoriaNombre(entidad.getCategoria().getNombre())
                .proveedorId(entidad.getProveedor().getId())
                .proveedorNombre(entidad.getProveedor().getNombre())
                .build();
    }

    public void updateEntityFromRequest(ProductoRequestDTO dto, Producto entidad) {
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setCodigoBarras(dto.getCodigoBarras());
        entidad.setPrecio(dto.getPrecio());
        entidad.setStock(dto.getStock());
    }
}
