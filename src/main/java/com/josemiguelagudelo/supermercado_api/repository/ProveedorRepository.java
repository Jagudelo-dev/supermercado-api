package com.josemiguelagudelo.supermercado_api.repository;

import com.josemiguelagudelo.supermercado_api.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Proveedor}.
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}