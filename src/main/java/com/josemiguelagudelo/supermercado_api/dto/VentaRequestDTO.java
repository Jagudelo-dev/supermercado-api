package com.josemiguelagudelo.supermercado_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO de entrada para crear una venta con todos sus detalles.
 */
@Data
public class VentaRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "La venta debe incluir al menos un detalle")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}
