package com.josemiguelagudelo.supermercado_api.repository;

import com.josemiguelagudelo.supermercado_api.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Venta.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    boolean existsByCliente_Id(Long clienteId);

    List<Venta> findByCliente_Id(Long clienteId);

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
