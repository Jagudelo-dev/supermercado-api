package com.josemiguelagudelo.supermercado_api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida con la representación de una venta.
 */
@Data
@Builder
public class VentaResponseDTO {

    private Long id;
    private Long clienteId;
    private String clienteDocumento;
    private LocalDateTime fecha;
    private BigDecimal total;
    private List<DetalleVentaResponseDTO> detalles;
}
