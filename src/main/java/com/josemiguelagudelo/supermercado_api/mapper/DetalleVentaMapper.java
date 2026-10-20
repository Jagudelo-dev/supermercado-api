package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.DetalleVentaResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.DetalleVenta;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir un detalle de venta a su DTO.
 */
@Component
public class DetalleVentaMapper {

    public DetalleVentaResponseDTO toResponse(DetalleVenta entidad) {
        return DetalleVentaResponseDTO.builder()
                .id(entidad.getId())
                .productoId(entidad.getProducto().getId())
                .productoNombre(entidad.getProducto().getNombre())
                .cantidad(entidad.getCantidad())
                .precioUnitario(entidad.getPrecioUnitario())
                .subtotal(entidad.getSubtotal())
                .build();
    }
}
