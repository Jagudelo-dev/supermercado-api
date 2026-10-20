package com.josemiguelagudelo.supermercado_api.dto;
import lombok.Data;
import lombok.Builder;

/**
 * DTO de salida con la representación de un proveedor expuesta por la API.
 */
@Data
@Builder
public class ProveedorResponseDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String correo;
}