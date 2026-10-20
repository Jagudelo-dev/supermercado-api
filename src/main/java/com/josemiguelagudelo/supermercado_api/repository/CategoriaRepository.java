package com.josemiguelagudelo.supermercado_api.repository;

import com.josemiguelagudelo.supermercado_api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Categoria}.
 * Hereda las operaciones CRUD estándar de {@link JpaRepository}.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
