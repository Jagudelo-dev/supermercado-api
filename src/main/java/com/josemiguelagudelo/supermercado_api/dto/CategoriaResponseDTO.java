package com.josemiguelagudelo.supermercado_api.dto;


import lombok.Data;
import lombok.Builder;


/**
 * DTO de salida con la representación de una categoría expuesta por la API.
 */
@Data
@Builder
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
}
