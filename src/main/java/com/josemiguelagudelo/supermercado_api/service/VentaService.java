package com.josemiguelagudelo.supermercado_api.service;

import com.josemiguelagudelo.supermercado_api.dto.DetalleVentaRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.VentaRequestDTO;
import com.josemiguelagudelo.supermercado_api.dto.VentaResponseDTO;
import com.josemiguelagudelo.supermercado_api.entity.Cliente;
import com.josemiguelagudelo.supermercado_api.entity.DetalleVenta;
import com.josemiguelagudelo.supermercado_api.entity.Producto;
import com.josemiguelagudelo.supermercado_api.entity.Venta;
import com.josemiguelagudelo.supermercado_api.exception.RecursoNoEncontradoException;
import com.josemiguelagudelo.supermercado_api.exception.ReglaNegocioException;
import com.josemiguelagudelo.supermercado_api.mapper.VentaMapper;
import com.josemiguelagudelo.supermercado_api.repository.ClienteRepository;
import com.josemiguelagudelo.supermercado_api.repository.ProductoRepository;
import com.josemiguelagudelo.supermercado_api.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de negocio para crear, consultar y eliminar ventas.
 */
@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listar() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO buscarPorId(Long id) {
        return ventaMapper.toResponse(obtenerVenta(id));
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> buscarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException(
                    "Cliente no encontrado con id: " + clienteId);
        }
        return ventaRepository.findByCliente_Id(clienteId).stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin).stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    @Transactional
    public VentaResponseDTO crear(VentaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con id: " + dto.getClienteId()));

        List<DetallePreparado> detallesPreparados = new ArrayList<>();
        Map<Long, Long> cantidadesPorProducto = new HashMap<>();
        Map<Long, Producto> productosPorId = new HashMap<>();

        for (DetalleVentaRequestDTO detalleDto : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + detalleDto.getProductoId()));

            int cantidad = detalleDto.getCantidad();
            BigDecimal precioUnitario = producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad))
                    .setScale(2, RoundingMode.HALF_UP);

            detallesPreparados.add(new DetallePreparado(producto, cantidad, precioUnitario, subtotal));
            cantidadesPorProducto.merge(producto.getId(), (long) cantidad, Long::sum);
            productosPorId.put(producto.getId(), producto);
        }

        cantidadesPorProducto.forEach((productoId, cantidad) -> {
            Producto producto = productosPorId.get(productoId);
            if (cantidad > producto.getStock()) {
                throw new ReglaNegocioException(
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
        });

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(BigDecimal.ZERO.setScale(2));
        for (DetallePreparado preparado : detallesPreparados) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(preparado.producto());
            detalle.setCantidad(preparado.cantidad());
            detalle.setPrecioUnitario(preparado.precioUnitario());
            detalle.setSubtotal(preparado.subtotal());
            venta.agregarDetalle(detalle);
            venta.setTotal(venta.getTotal().add(preparado.subtotal()));
        }

        cantidadesPorProducto.forEach((productoId, cantidad) -> {
            Producto producto = productosPorId.get(productoId);
            producto.setStock((int) (producto.getStock() - cantidad));
        });

        return ventaMapper.toResponse(ventaRepository.save(venta));
    }

    @Transactional
    public void eliminar(Long id) {
        Venta venta = obtenerVenta(id);
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
        }
        ventaRepository.delete(venta);
    }

    private Venta obtenerVenta(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada con id: " + id));
    }

    private record DetallePreparado(
            Producto producto,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal) {
    }
}
