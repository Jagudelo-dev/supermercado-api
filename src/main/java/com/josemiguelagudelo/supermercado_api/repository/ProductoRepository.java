package com.josemiguelagudelo.supermercado_api.repository;

import com.josemiguelagudelo.supermercado_api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Producto.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarrasAndIdNot(String codigoBarras, Long id);

    boolean existsByCategoria_Id(Long categoriaId);

    boolean existsByProveedor_Id(Long proveedorId);
}
