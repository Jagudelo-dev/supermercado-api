package com.josemiguelagudelo.supermercado_api.service;

import com.josemiguelagudelo.supermercado_api.dto.ClienteRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ClienteResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Cliente;
import com.josemiguelagudelo.supermercado_api.exception.RecursoNoEncontradoException;
import com.josemiguelagudelo.supermercado_api.exception.ReglaNegocioException;
import com.josemiguelagudelo.supermercado_api.mapper.ClienteMapper;
import com.josemiguelagudelo.supermercado_api.repository.ClienteRepository;
import com.josemiguelagudelo.supermercado_api.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de clientes.
 */
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return clienteMapper.toResponse(obtenerCliente(id));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento)
                .map(clienteMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con documento: " + documento));
    }

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByDocumento(dto.getDocumento())) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con el documento: " + dto.getDocumento());
        }
        return clienteMapper.toResponse(clienteRepository.save(clienteMapper.toEntity(dto)));
    }

    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente entidad = obtenerCliente(id);
        if (clienteRepository.existsByDocumentoAndIdNot(dto.getDocumento(), id)) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con el documento: " + dto.getDocumento());
        }
        clienteMapper.updateEntityFromRequest(dto, entidad);
        return clienteMapper.toResponse(clienteRepository.save(entidad));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerCliente(id);
        if (ventaRepository.existsByCliente_Id(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar: el cliente tiene ventas asociadas");
        }
        clienteRepository.delete(cliente);
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con id: " + id));
    }
}
