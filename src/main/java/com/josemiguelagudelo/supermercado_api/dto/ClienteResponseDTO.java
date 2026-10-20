package com.josemiguelagudelo.supermercado_api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de salida con la representación de un cliente.
 */
@Data
@Builder
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String documento;
    private String telefono;
}
