package com.josemiguelagudelo.supermercado_api.service;

import com.josemiguelagudelo.supermercado_api.dto.CategoriaRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.CategoriaResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Categoria;
import com.josemiguelagudelo.supermercado_api.exception.RecursoNoEncontradoException;
import com.josemiguelagudelo.supermercado_api.exception.ReglaNegocioException;
import com.josemiguelagudelo.supermercado_api.mapper.CategoriaMapper;
import com.josemiguelagudelo.supermercado_api.repository.CategoriaRepository;
import com.josemiguelagudelo.supermercado_api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de categorías.
 * Coordina el repositorio, el mapper y las reglas de negocio.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + id));
        return categoriaMapper.toResponse(categoria);
    }

    @Transactional
    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        Categoria entidad = categoriaMapper.toEntity(dto);
        Categoria guardada = categoriaRepository.save(entidad);
        return categoriaMapper.toResponse(guardada);
    }

    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        Categoria entidad = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + id));
        categoriaMapper.updateEntityFromRequest(dto, entidad);
        Categoria actualizada = categoriaRepository.save(entidad);
        return categoriaMapper.toResponse(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + id));
        if (productoRepository.existsByCategoria_Id(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar: la categoría tiene productos asociados");
        }
        categoriaRepository.deleteById(id);
    }
}
