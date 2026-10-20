package com.josemiguelagudelo.supermercado_api.service;

import com.josemiguelagudelo.supermercado_api.dto.ProveedorRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.ProveedorResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Proveedor;
import com.josemiguelagudelo.supermercado_api.exception.RecursoNoEncontradoException;
import com.josemiguelagudelo.supermercado_api.exception.ReglaNegocioException;
import com.josemiguelagudelo.supermercado_api.mapper.ProveedorMapper;
import com.josemiguelagudelo.supermercado_api.repository.ProductoRepository;
import com.josemiguelagudelo.supermercado_api.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de proveedores.
 */
@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(proveedorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Proveedor no encontrado con id: " + id));
        return proveedorMapper.toResponse(proveedor);
    }

    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        Proveedor entidad = proveedorMapper.toEntity(dto);
        Proveedor guardado = proveedorRepository.save(entidad);
        return proveedorMapper.toResponse(guardado);
    }

    @Transactional
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor entidad = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Proveedor no encontrado con id: " + id));
        proveedorMapper.updateEntityFromRequest(dto, entidad);
        Proveedor actualizado = proveedorRepository.save(entidad);
        return proveedorMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Proveedor no encontrado con id: " + id));
        if (productoRepository.existsByProveedor_Id(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar: el proveedor tiene productos asociados");
        }
        proveedorRepository.deleteById(id);
    }
}