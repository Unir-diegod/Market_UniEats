package com.remington.unieats.marketplace.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.remington.unieats.marketplace.dto.AnalyticsVendedorDTO;
import com.remington.unieats.marketplace.dto.AnalyticsVendedorDTO.ProductoVendidoDTO;
import com.remington.unieats.marketplace.dto.AnalyticsVendedorDTO.VentaDiariaDTO;
import com.remington.unieats.marketplace.dto.CategoriaOpcionCreacionDTO;
import com.remington.unieats.marketplace.dto.HorarioUpdateDTO;
import com.remington.unieats.marketplace.dto.OpcionCreacionDTO;
import com.remington.unieats.marketplace.dto.PedidoVendedorDTO;
import com.remington.unieats.marketplace.dto.TiendaCreacionDTO;
import com.remington.unieats.marketplace.dto.TiendaUpdateDTO;
import com.remington.unieats.marketplace.model.entity.CategoriaOpcion;
import com.remington.unieats.marketplace.model.entity.DetallePedido;
import com.remington.unieats.marketplace.model.entity.Horario;
import com.remington.unieats.marketplace.model.entity.Opcion;
import com.remington.unieats.marketplace.model.entity.Pedido;
import com.remington.unieats.marketplace.model.entity.Producto;
import com.remington.unieats.marketplace.model.entity.Tienda;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.enums.DiaSemana;
import com.remington.unieats.marketplace.model.repository.CategoriaOpcionRepository;
import com.remington.unieats.marketplace.model.repository.HorarioRepository;
import com.remington.unieats.marketplace.model.repository.PedidoRepository;
import com.remington.unieats.marketplace.model.repository.ProductoRepository;
import com.remington.unieats.marketplace.model.repository.TiendaRepository;

@Service
public class VendedorServiceImpl implements VendedorService {

    @Autowired private TiendaRepository tiendaRepository;
    @Autowired private HorarioRepository horarioRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private CategoriaOpcionRepository categoriaOpcionRepository;
    @Autowired private ImageService imageService; // 🔄 Usando ImageService híbrido

    @Override
    public Optional<Tienda> findTiendaByVendedor(Usuario vendedor) {
        return tiendaRepository.findByVendedor(vendedor);
    }

    @Override
    @Transactional
    public Tienda crearTienda(TiendaCreacionDTO tiendaDTO, Usuario vendedor, MultipartFile logoFile) {
        if (tiendaRepository.findByVendedor(vendedor).isPresent()) {
            throw new IllegalStateException("Este vendedor ya tiene una tienda registrada.");
        }
        Tienda nuevaTienda = new Tienda();
        nuevaTienda.setNombre(tiendaDTO.getNombre());
        nuevaTienda.setNit(tiendaDTO.getNit());
        nuevaTienda.setDescripcion(tiendaDTO.getDescripcion());
        nuevaTienda.setVendedor(vendedor);

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                String logoUrl = imageService.uploadImage(logoFile, "logos");
                nuevaTienda.setLogoUrl(logoUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error al subir logo: " + e.getMessage(), e);
            }
        }
        
        Tienda tiendaGuardada = tiendaRepository.save(nuevaTienda);

        for (DiaSemana dia : DiaSemana.values()) {
            Horario horario = new Horario();
            horario.setDia(dia);
            horario.setAbierto(false);
            horario.setTienda(tiendaGuardada);
            horarioRepository.save(horario);
        }
        return tiendaGuardada;
    }

    @Override
    @Transactional
    public Tienda actualizarTienda(Tienda tienda, TiendaUpdateDTO updateDTO, MultipartFile logoFile) {
        tienda.setNombre(updateDTO.getNombre());
        tienda.setDescripcion(updateDTO.getDescripcion());

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                // Eliminar logo anterior si existe
                if (tienda.getLogoUrl() != null && !tienda.getLogoUrl().isEmpty()) {
                    imageService.deleteImage(tienda.getLogoUrl());
                }
                String logoUrl = imageService.uploadImage(logoFile, "logos");
                tienda.setLogoUrl(logoUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar logo: " + e.getMessage(), e);
            }
        }
        return tiendaRepository.save(tienda);
    }

    @Override
    public List<Horario> findHorariosByTienda(Tienda tienda) {
        return horarioRepository.findByTienda(tienda);
    }

    @Override
    @Transactional
    public void actualizarHorarios(Tienda tienda, List<HorarioUpdateDTO> horariosDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (HorarioUpdateDTO dto : horariosDTO) {
            Horario horario = horarioRepository.findByTiendaAndDia(tienda, dto.getDia())
                    .orElseThrow(() -> new IllegalStateException("Horario no encontrado para el día: " + dto.getDia()));
            
            horario.setAbierto(dto.isAbierto());
            if (dto.isAbierto()) {
                horario.setHoraApertura(LocalTime.parse(dto.getHoraApertura(), formatter));
                horario.setHoraCierre(LocalTime.parse(dto.getHoraCierre(), formatter));
            } else {
                horario.setHoraApertura(null);
                horario.setHoraCierre(null);
            }
            horarioRepository.save(horario);
        }
    }

    @Override
    public List<PedidoVendedorDTO> getPedidosDeLaTienda(Tienda tienda) {
        List<Pedido> pedidos = pedidoRepository.findByTiendaOrderByFechaCreacionDesc(tienda);
        return pedidos.stream()
                .map(this::convertirAPedidoVendedorDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CategoriaOpcion> getCategoriasDeOpciones(Tienda tienda) {
        return categoriaOpcionRepository.findByTienda(tienda);
    }

    @Override
    @Transactional
    public CategoriaOpcion crearCategoriaConOpciones(CategoriaOpcionCreacionDTO dto, Tienda tienda) {
        CategoriaOpcion nuevaCategoria = new CategoriaOpcion();
        nuevaCategoria.setNombre(dto.getNombre());
        nuevaCategoria.setTienda(tienda);

        List<Opcion> opciones = new ArrayList<>();
        if (dto.getOpciones() != null) {
            for (OpcionCreacionDTO opcionDTO : dto.getOpciones()) {
                Opcion nuevaOpcion = new Opcion();
                nuevaOpcion.setNombre(opcionDTO.getNombre());
                nuevaOpcion.setPrecioAdicional(opcionDTO.getPrecioAdicional());
                nuevaOpcion.setCategoria(nuevaCategoria);
                opciones.add(nuevaOpcion);
            }
        }
        nuevaCategoria.setOpciones(opciones);
        
        return categoriaOpcionRepository.save(nuevaCategoria);
    }

    @Override
    @Transactional
    public void asignarCategoriaAProducto(Integer productoId, Integer categoriaId) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        CategoriaOpcion categoria = categoriaOpcionRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!producto.getTienda().getId().equals(categoria.getTienda().getId())) {
            throw new IllegalStateException("La categoría no pertenece a la tienda de este producto.");
        }

        producto.getCategoriasDeOpciones().add(categoria);
        productoRepository.save(producto);
    }
    
    private PedidoVendedorDTO convertirAPedidoVendedorDTO(Pedido pedido) {
        PedidoVendedorDTO dto = new PedidoVendedorDTO();
        dto.setId(pedido.getId());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setNombreComprador(pedido.getComprador().getNombre() + " " + pedido.getComprador().getApellido());

        // Mapear nuevos campos de entrega y pago
        dto.setTipoEntrega(pedido.getTipoEntrega());
        dto.setTipoPago(pedido.getTipoPago());
        dto.setNotasGenerales(pedido.getNotasGenerales());
        dto.setNotasDomicilio(pedido.getNotasDomicilio());

        List<PedidoVendedorDTO.DetallePedidoVendedorDTO> detallesDTO = pedido.getDetalles().stream()
                .map(this::convertirADetalleDTO)
                .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);
        
        return dto;
    }

    private PedidoVendedorDTO.DetallePedidoVendedorDTO convertirADetalleDTO(DetallePedido detalle) {
        PedidoVendedorDTO.DetallePedidoVendedorDTO dto = new PedidoVendedorDTO.DetallePedidoVendedorDTO();
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        
        // Mapear opciones seleccionadas
        if (detalle.getOpcionesSeleccionadas() != null && !detalle.getOpcionesSeleccionadas().isEmpty()) {
            String opciones = detalle.getOpcionesSeleccionadas().stream()
                    .map(opcion -> opcion.getNombre())
                    .collect(Collectors.joining(", "));
            dto.setOpcionesSeleccionadas(opciones);
        }
        
        return dto;
    }

    @Override
    @Transactional
    public void actualizarEstadoTienda(Integer tiendaId, Boolean estaAbierta) {
        Tienda tienda = tiendaRepository.findById(tiendaId)
            .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        tienda.setEstaAbierta(estaAbierta);
        tiendaRepository.save(tienda);
    }

    @Override
    public Double calcularVentasHoy(Tienda tienda) {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDelDia = hoy.atStartOfDay();
        LocalDateTime finDelDia = hoy.atTime(23, 59, 59);
        
        List<Pedido> pedidosHoy = pedidoRepository.findByTiendaAndFechaCreacionBetween(
            tienda, inicioDelDia, finDelDia);
        
        return pedidosHoy.stream()
            .filter(p -> p.getEstado().name().equals("COMPLETADO"))
            .mapToDouble(p -> p.getTotal().doubleValue())
            .sum();
    }

    @Override
    public Integer contarPedidosNuevos(Tienda tienda) {
        return pedidoRepository.findByTiendaAndEstado(tienda, 
            com.remington.unieats.marketplace.model.enums.EstadoPedido.PENDIENTE).size();
    }

    @Override
    public Integer contarPedidosCompletadosHoy(Tienda tienda) {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDelDia = hoy.atStartOfDay();
        LocalDateTime finDelDia = hoy.atTime(23, 59, 59);
        
        List<Pedido> pedidosHoy = pedidoRepository.findByTiendaAndFechaCreacionBetween(
            tienda, inicioDelDia, finDelDia);
        
        return (int) pedidosHoy.stream()
            .filter(p -> p.getEstado().name().equals("COMPLETADO"))
            .count();
    }

    @Override
    public AnalyticsVendedorDTO obtenerAnalytics(Tienda tienda) {
        AnalyticsVendedorDTO analytics = new AnalyticsVendedorDTO();
        
        // Obtener todos los pedidos completados de la tienda
        List<Pedido> pedidosCompletados = pedidoRepository.findByTiendaOrderByFechaCreacionDesc(tienda).stream()
            .filter(p -> p.getEstado().name().equals("COMPLETADO"))
            .collect(Collectors.toList());
        
        // KPIs principales
        BigDecimal ventasTotales = pedidosCompletados.stream()
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        analytics.setVentasTotales(ventasTotales);
        analytics.setTotalPedidos((long) pedidosCompletados.size());
        
        BigDecimal ventaPromedio = pedidosCompletados.isEmpty() 
            ? BigDecimal.ZERO 
            : ventasTotales.divide(BigDecimal.valueOf(pedidosCompletados.size()), 2, java.math.RoundingMode.HALF_UP);
        analytics.setVentaPromedio(ventaPromedio);
        
        Long totalProductos = (long) productoRepository.findByTienda(tienda).size();
        analytics.setTotalProductos(totalProductos);
        
        // Ventas por día (últimos 30 días)
        LocalDate hoy = LocalDate.now();
        List<VentaDiariaDTO> ventasPorDia = new ArrayList<>();
        
        for (int i = 29; i >= 0; i--) {
            LocalDate fecha = hoy.minusDays(i);
            LocalDateTime inicioDelDia = fecha.atStartOfDay();
            LocalDateTime finDelDia = fecha.atTime(23, 59, 59);
            
            List<Pedido> pedidosDelDia = pedidosCompletados.stream()
                .filter(p -> p.getFechaCreacion().isAfter(inicioDelDia) && 
                            p.getFechaCreacion().isBefore(finDelDia))
                .collect(Collectors.toList());
            
            BigDecimal ventasDelDia = pedidosDelDia.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            ventasPorDia.add(new VentaDiariaDTO(
                fecha.toString(), 
                ventasDelDia, 
                (long) pedidosDelDia.size()
            ));
        }
        analytics.setVentasPorDia(ventasPorDia);
        
        // Top 5 productos más vendidos
        Map<String, ProductoVendidoStats> statsMap = new HashMap<>();
        
        for (Pedido pedido : pedidosCompletados) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                String nombreProducto = detalle.getProducto().getNombre();
                statsMap.putIfAbsent(nombreProducto, new ProductoVendidoStats());
                ProductoVendidoStats stats = statsMap.get(nombreProducto);
                stats.cantidad += detalle.getCantidad();
                stats.ingresos = stats.ingresos.add(
                    detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()))
                );
            }
        }
        
        List<ProductoVendidoDTO> topProductos = statsMap.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue().cantidad, e1.getValue().cantidad))
            .limit(5)
            .map(e -> new ProductoVendidoDTO(
                e.getKey(), 
                e.getValue().cantidad, 
                e.getValue().ingresos
            ))
            .collect(Collectors.toList());
        analytics.setTopProductos(topProductos);
        
        // Distribución de pedidos por estado
        Map<String, Long> pedidosPorEstado = new HashMap<>();
        List<Pedido> todosPedidos = pedidoRepository.findByTiendaOrderByFechaCreacionDesc(tienda);
        
        for (Pedido pedido : todosPedidos) {
            String estado = pedido.getEstado().name();
            pedidosPorEstado.put(estado, pedidosPorEstado.getOrDefault(estado, 0L) + 1);
        }
        analytics.setPedidosPorEstado(pedidosPorEstado);
        
        // Variación de ventas (comparar últimos 30 días vs 30 días anteriores)
        LocalDateTime hace30Dias = hoy.minusDays(30).atStartOfDay();
        LocalDateTime hace60Dias = hoy.minusDays(60).atStartOfDay();
        
        BigDecimal ventasUltimos30 = pedidosCompletados.stream()
            .filter(p -> p.getFechaCreacion().isAfter(hace30Dias))
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal ventas30Anteriores = pedidosCompletados.stream()
            .filter(p -> p.getFechaCreacion().isAfter(hace60Dias) && 
                        p.getFechaCreacion().isBefore(hace30Dias))
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal variacion = BigDecimal.ZERO;
        if (ventas30Anteriores.compareTo(BigDecimal.ZERO) > 0) {
            variacion = ventasUltimos30.subtract(ventas30Anteriores)
                .divide(ventas30Anteriores, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
        analytics.setVariacionVentas(variacion);
        
        return analytics;
    }
    
    // Clase auxiliar para estadísticas de productos
    private static class ProductoVendidoStats {
        long cantidad = 0;
        BigDecimal ingresos = BigDecimal.ZERO;
    }
}