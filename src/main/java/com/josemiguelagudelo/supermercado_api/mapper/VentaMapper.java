package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.VentaResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Venta;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir una venta a su DTO.
 */
@Component
public class VentaMapper {

    private final DetalleVentaMapper detalleVentaMapper;

    public VentaMapper(DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaMapper = detalleVentaMapper;
    }

    public VentaResponseDTO toResponse(Venta entidad) {
        return VentaResponseDTO.builder()
                .id(entidad.getId())
                .clienteId(entidad.getCliente().getId())
                .clienteDocumento(entidad.getCliente().getDocumento())
                .fecha(entidad.getFecha())
                .total(entidad.getTotal())
                .detalles(entidad.getDetalles().stream()
                        .map(detalleVentaMapper::toResponse)
                        .toList())
                .build();
    }
}
