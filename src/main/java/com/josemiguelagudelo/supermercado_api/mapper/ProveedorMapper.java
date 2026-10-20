package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.ProveedorRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ProveedorResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Proveedor;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir entre la entidad {@link Proveedor}
 * y sus DTOs de petición y respuesta.
 */
@Component
public class ProveedorMapper {

    public Proveedor toEntity(ProveedorRequestDTO dto) {
        return Proveedor.builder()
                .nombre(dto.getNombre())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .build();
    }

    public ProveedorResponseDTO toResponse(Proveedor entidad) {
        return ProveedorResponseDTO.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .telefono(entidad.getTelefono())
                .correo(entidad.getCorreo())
                .build();
    }

    public void updateEntityFromRequest(ProveedorRequestDTO dto, Proveedor entidad) {
        entidad.setNombre(dto.getNombre());
        entidad.setTelefono(dto.getTelefono());
        entidad.setCorreo(dto.getCorreo());
    }
}