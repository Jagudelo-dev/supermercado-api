package com.josemiguelagudelo.supermercado_api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de salida con la representación de un detalle de venta.
 */
@Data
@Builder
public class DetalleVentaResponseDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
