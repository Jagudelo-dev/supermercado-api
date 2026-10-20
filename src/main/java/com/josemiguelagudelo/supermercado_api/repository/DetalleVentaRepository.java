package com.josemiguelagudelo.supermercado_api.repository;

import com.josemiguelagudelo.supermercado_api.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad DetalleVenta.
 */
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    boolean existsByProducto_Id(Long productoId);
}
