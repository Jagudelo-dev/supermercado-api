package com.josemiguelagudelo.supermercado_api.mapper;

import com.josemiguelagudelo.supermercado_api.dto.ClienteRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ClienteResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Cliente;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir entre Cliente y sus DTOs.
 */
@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        Cliente entidad = new Cliente();
        updateEntityFromRequest(dto, entidad);
        return entidad;
    }

    public ClienteResponseDTO toResponse(Cliente entidad) {
        return ClienteResponseDTO.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .documento(entidad.getDocumento())
                .telefono(entidad.getTelefono())
                .build();
    }

    public void updateEntityFromRequest(ClienteRequestDTO dto, Cliente entidad) {
        entidad.setNombre(dto.getNombre());
        entidad.setDocumento(dto.getDocumento());
        entidad.setTelefono(dto.getTelefono());
    }
}
