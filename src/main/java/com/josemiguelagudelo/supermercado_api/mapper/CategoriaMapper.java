package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.CategoriaRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.CategoriaResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Categoria;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir entre la entidad {@link Categoria}
 * y sus DTOs de petición y respuesta.
 */
@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequestDTO dto) {
        return Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public CategoriaResponseDTO toResponse(Categoria entidad) {
        return CategoriaResponseDTO.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .descripcion(entidad.getDescripcion())
                .build();
    }

    public void updateEntityFromRequest(CategoriaRequestDTO dto, Categoria entidad) {
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
    }
}
