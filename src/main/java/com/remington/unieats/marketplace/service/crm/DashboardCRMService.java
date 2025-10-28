package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.dto.crm.DashboardCRMDTO;
import com.remington.unieats.marketplace.dto.crm.DashboardCRMDTO.TopProductoDTO;
import com.remington.unieats.marketplace.dto.crm.DashboardCRMDTO.TopVendedorDTO;
import com.remington.unieats.marketplace.dto.crm.DashboardCRMDTO.TendenciaDiaria;
import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.model.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generar métricas del Dashboard CRM
 */
@Service
public class DashboardCRMService {

    private static final Logger log = LoggerFactory.getLogger(DashboardCRMService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private InteraccionClienteRepository interaccionRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    /**
     * Obtener todas las métricas del Dashboard CRM
     */
    public DashboardCRMDTO obtenerMetricasDashboard() {
        log.info("📊 Generando métricas del Dashboard CRM...");
        
        DashboardCRMDTO dashboard = new DashboardCRMDTO();
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime hace30Dias = ahora.minusDays(30);
        LocalDateTime hace60Dias = ahora.minusDays(60);
        LocalDateTime hace7Dias = ahora.minusDays(7);

        // ========== MÉTRICAS DE CLIENTES ==========
        log.debug("📝 Calculando métricas de clientes...");
        dashboard.setTotalClientes(clienteRepository.count());
        dashboard.setClientesActivos((long) clienteRepository.findClientesActivosDesde(hace30Dias).size());
        dashboard.setClientesInactivos((long) clienteRepository.findClientesInactivos(hace60Dias).size());
        dashboard.setClientesNuevosUltimoMes(clienteRepository.countClientesNuevos(hace30Dias));

        // ========== MÉTRICAS FINANCIERAS ==========
        log.debug("💰 Calculando métricas financieras...");
        List<Cliente> todosClientes = clienteRepository.findAll();
        double valorTotal = todosClientes.stream()
                .mapToDouble(Cliente::getValorTotalCompras)
                .sum();
        dashboard.setValorTotalVentas(valorTotal);
        dashboard.setTicketPromedio(clienteRepository.getTicketPromedioGeneral());

        // Ingresos último mes y crecimiento
        Double ingresosUltimoMes = calcularIngresosEnPeriodo(hace30Dias, ahora);
        Double ingresosMesAnterior = calcularIngresosEnPeriodo(hace60Dias, hace30Dias);
        dashboard.setIngresosUltimoMes(ingresosUltimoMes);
        
        if (ingresosMesAnterior > 0) {
            double crecimiento = ((ingresosUltimoMes - ingresosMesAnterior) / ingresosMesAnterior) * 100;
            dashboard.setCrecimientoVentas(crecimiento);
        } else {
            dashboard.setCrecimientoVentas(100.0);
        }

        // ========== MÉTRICAS DE CAMPAÑAS ==========
        log.debug("📧 Calculando métricas de campañas...");
        dashboard.setTotalCampanas(campanaRepository.count());
        dashboard.setCampanasActivas(campanaRepository.countByEstado("ACTIVA"));
        dashboard.setCampanasProgramadas(campanaRepository.countByEstado("PROGRAMADA"));
        dashboard.setCampanasFinalizadas(campanaRepository.countByEstado("FINALIZADA"));

        Map<String, Long> campanasPorEstado = new HashMap<>();
        campanasPorEstado.put("BORRADOR", campanaRepository.countByEstado("BORRADOR"));
        campanasPorEstado.put("PROGRAMADA", campanaRepository.countByEstado("PROGRAMADA"));
        campanasPorEstado.put("ACTIVA", campanaRepository.countByEstado("ACTIVA"));
        campanasPorEstado.put("PAUSADA", campanaRepository.countByEstado("PAUSADA"));
        campanasPorEstado.put("FINALIZADA", campanaRepository.countByEstado("FINALIZADA"));
        dashboard.setCampanasPorEstado(campanasPorEstado);

        // ========== MÉTRICAS DE EMAILS ==========
        log.debug("✉️ Calculando métricas de emails...");
        List<Campana> todasCampanas = campanaRepository.findAll();
        
        int emailsTotales = todasCampanas.stream()
                .mapToInt(c -> c.getEnviosTotales() != null ? c.getEnviosTotales() : 0)
                .sum();
        dashboard.setEmailsEnviadosTotal(emailsTotales);

        // Emails último mes (filtrando por fecha de creación manualmente)
        int emailsUltimoMes = todasCampanas.stream()
                .filter(c -> c.getFechaCreacion() != null && c.getFechaCreacion().isAfter(hace30Dias))
                .mapToInt(c -> c.getEnviosTotales() != null ? c.getEnviosTotales() : 0)
                .sum();
        dashboard.setEmailsEnviadosUltimoMes(emailsUltimoMes);

        // Tasas promedio
        double tasaAperturaPromedio = todasCampanas.stream()
                .filter(c -> c.getTasaApertura() != null && c.getTasaApertura() > 0)
                .mapToDouble(Campana::getTasaApertura)
                .average()
                .orElse(0.0);
        dashboard.setTasaAperturaPromedio(tasaAperturaPromedio);

        double tasaClicsPromedio = todasCampanas.stream()
                .filter(c -> c.getTasaClics() != null && c.getTasaClics() > 0)
                .mapToDouble(Campana::getTasaClics)
                .average()
                .orElse(0.0);
        dashboard.setTasaClicsPromedio(tasaClicsPromedio);

        int conversionesEmail = todasCampanas.stream()
                .mapToInt(c -> c.getConversiones() != null ? c.getConversiones() : 0)
                .sum();
        dashboard.setConversionesEmail(conversionesEmail);

        // ========== MÉTRICAS DE SEGMENTACIÓN ==========
        log.debug("👥 Calculando métricas de segmentación...");
        dashboard.setTotalSegmentos(segmentoRepository.count());
        dashboard.setSegmentosActivos(segmentoRepository.countSegmentosActivos());

        Map<String, Long> clientesPorSegmento = new HashMap<>();
        List<Segmento> segmentos = segmentoRepository.findAll();
        for (Segmento segmento : segmentos) {
            if (segmento.getActivo() != null && segmento.getActivo()) {
                clientesPorSegmento.put(
                    segmento.getNombre(), 
                    (long) segmento.getClientes().size()
                );
            }
        }
        dashboard.setClientesPorSegmento(clientesPorSegmento);

        // ========== ROI Y CONVERSIONES ==========
        log.debug("💹 Calculando ROI y conversiones...");
        double roiPromedio = todasCampanas.stream()
                .filter(c -> c.getRoi() != null && c.getRoi() > 0)
                .mapToDouble(Campana::getRoi)
                .average()
                .orElse(0.0);
        dashboard.setRoiPromedio(roiPromedio);

        dashboard.setConversionesTotales(conversionesEmail);

        double inversionTotal = todasCampanas.stream()
                .filter(c -> c.getPresupuesto() != null)
                .mapToDouble(Campana::getPresupuesto)
                .sum();
        dashboard.setInversionTotalCampanas(inversionTotal);

        double retornoTotal = todasCampanas.stream()
                .filter(c -> c.getIngresosGenerados() != null)
                .mapToDouble(Campana::getIngresosGenerados)
                .sum();
        dashboard.setRetornoTotalCampanas(retornoTotal);

        if (emailsTotales > 0) {
            double tasaConversion = (conversionesEmail * 100.0) / emailsTotales;
            dashboard.setTasaConversionPromedio(tasaConversion);
        } else {
            dashboard.setTasaConversionPromedio(0.0);
        }

        // ========== TOP PRODUCTOS ==========
        log.debug("🏆 Calculando top productos...");
        dashboard.setTopProductos(obtenerTopProductos(10));

        // ========== TOP VENDEDORES ==========
        log.debug("⭐ Calculando top vendedores...");
        dashboard.setTopVendedores(obtenerTopVendedores(10));

        // ========== DISTRIBUCIÓN POR NIVEL ==========
        log.debug("📊 Calculando distribución por nivel...");
        Map<String, Long> clientesPorNivel = new HashMap<>();
        clientesPorNivel.put("BRONCE", (long) clienteRepository.findByNivelCliente("BRONCE").size());
        clientesPorNivel.put("PLATA", (long) clienteRepository.findByNivelCliente("PLATA").size());
        clientesPorNivel.put("ORO", (long) clienteRepository.findByNivelCliente("ORO").size());
        clientesPorNivel.put("PLATINUM", (long) clienteRepository.findByNivelCliente("PLATINUM").size());
        dashboard.setClientesPorNivel(clientesPorNivel);

        // ========== INTERACCIONES ==========
        log.debug("📞 Calculando interacciones...");
        dashboard.setTotalInteracciones(interaccionRepository.count());

        // Obtener interacciones recientes manualmente
        List<InteraccionCliente> interaccionesRecientes = interaccionRepository.findAll().stream()
                .filter(i -> i.getFechaInteraccion().isAfter(hace30Dias))
                .collect(Collectors.toList());

        Map<String, Long> interaccionesPorTipo = interaccionesRecientes.stream()
                .collect(Collectors.groupingBy(
                    InteraccionCliente::getTipo,
                    Collectors.counting()
                ));
        dashboard.setInteraccionesPorTipo(interaccionesPorTipo);

        Map<String, Long> interaccionesPorCanal = interaccionesRecientes.stream()
                .collect(Collectors.groupingBy(
                    InteraccionCliente::getCanal,
                    Collectors.counting()
                ));
        dashboard.setInteraccionesPorCanal(interaccionesPorCanal);

        // ========== TENDENCIAS (últimos 7 días) ==========
        log.debug("📈 Calculando tendencias...");
        dashboard.setTendenciaVentas(calcularTendenciaVentas(hace7Dias, ahora));
        dashboard.setTendenciaClientes(calcularTendenciaClientes(hace7Dias, ahora));

        log.info("✅ Métricas del Dashboard CRM generadas exitosamente");
        return dashboard;
    }

    /**
     * Calcular ingresos en un periodo
     */
    private Double calcularIngresosEnPeriodo(LocalDateTime desde, LocalDateTime hasta) {
        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(p -> p.getFechaCreacion().isAfter(desde) && p.getFechaCreacion().isBefore(hasta))
                .collect(Collectors.toList());
        return pedidos.stream()
                .mapToDouble(p -> p.getTotal().doubleValue())
                .sum();
    }

    /**
     * Obtener top productos más vendidos
     */
    private List<TopProductoDTO> obtenerTopProductos(int limit) {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();
        
        Map<Integer, List<DetallePedido>> productoDetalles = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getProducto().getId()));

        List<TopProductoDTO> topProductos = new ArrayList<>();
        
        for (Map.Entry<Integer, List<DetallePedido>> entry : productoDetalles.entrySet()) {
            List<DetallePedido> detallesProducto = entry.getValue();
            if (!detallesProducto.isEmpty()) {
                Producto producto = detallesProducto.get(0).getProducto();
                
                long cantidadVendida = detallesProducto.stream()
                        .mapToLong(DetallePedido::getCantidad)
                        .sum();
                
                double ingresoTotal = detallesProducto.stream()
                        .mapToDouble(d -> d.getPrecioUnitario().doubleValue() * d.getCantidad())
                        .sum();

                String vendedorNombre = producto.getTienda() != null && producto.getTienda().getVendedor() != null 
                    ? producto.getTienda().getVendedor().getNombre() + " " + producto.getTienda().getVendedor().getApellido()
                    : "Sin vendedor";

                TopProductoDTO dto = new TopProductoDTO(
                    Long.valueOf(producto.getId()),
                    producto.getNombre(),
                    producto.getClasificacion() != null ? producto.getClasificacion().toString() : "Sin categoría",
                    cantidadVendida,
                    ingresoTotal,
                    vendedorNombre
                );
                
                topProductos.add(dto);
            }
        }

        return topProductos.stream()
                .sorted(Comparator.comparing(TopProductoDTO::getCantidadVendida).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Obtener top tiendas/vendedores
     */
    private List<TopVendedorDTO> obtenerTopVendedores(int limit) {
        List<Tienda> tiendas = tiendaRepository.findAll();
        List<TopVendedorDTO> topVendedores = new ArrayList<>();

        for (Tienda tienda : tiendas) {
            List<Pedido> pedidosTienda = pedidoRepository.findAll().stream()
                    .filter(p -> p.getTienda().getId().equals(tienda.getId()))
                    .collect(Collectors.toList());
            
            long totalPedidos = pedidosTienda.size();
            
            double ingresoTotal = pedidosTienda.stream()
                    .mapToDouble(p -> p.getTotal().doubleValue())
                    .sum();

            Set<Integer> clientesUnicos = pedidosTienda.stream()
                    .map(p -> p.getComprador().getId())
                    .collect(Collectors.toSet());

            String nombreVendedor = tienda.getVendedor() != null 
                ? tienda.getVendedor().getNombre() + " " + tienda.getVendedor().getApellido()
                : "Sin vendedor";

            TopVendedorDTO dto = new TopVendedorDTO(
                Long.valueOf(tienda.getId()),
                nombreVendedor,
                tienda.getNombre(),
                totalPedidos,
                ingresoTotal,
                0.0, // TODO: Implementar calificación promedio si existe
                (long) clientesUnicos.size()
            );

            topVendedores.add(dto);
        }

        return topVendedores.stream()
                .sorted(Comparator.comparing(TopVendedorDTO::getIngresoTotal).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calcular tendencia de ventas diarias
     */
    private List<TendenciaDiaria> calcularTendenciaVentas(LocalDateTime desde, LocalDateTime hasta) {
        List<TendenciaDiaria> tendencia = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate fechaInicio = desde.toLocalDate();
        LocalDate fechaFin = hasta.toLocalDate();

        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            LocalDateTime inicioDia = fecha.atStartOfDay();
            LocalDateTime finDia = fecha.atTime(23, 59, 59);

            List<Pedido> pedidosDia = pedidoRepository.findAll().stream()
                    .filter(p -> p.getFechaCreacion().isAfter(inicioDia) && p.getFechaCreacion().isBefore(finDia))
                    .collect(Collectors.toList());
            
            double ventasDia = pedidosDia.stream()
                    .mapToDouble(p -> p.getTotal().doubleValue())
                    .sum();

            tendencia.add(new TendenciaDiaria(
                fecha.format(formatter),
                ventasDia,
                (long) pedidosDia.size()
            ));
        }

        return tendencia;
    }

    /**
     * Calcular tendencia de nuevos clientes diarios
     */
    private List<TendenciaDiaria> calcularTendenciaClientes(LocalDateTime desde, LocalDateTime hasta) {
        List<TendenciaDiaria> tendencia = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate fechaInicio = desde.toLocalDate();
        LocalDate fechaFin = hasta.toLocalDate();

        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            LocalDateTime inicioDia = fecha.atStartOfDay();
            LocalDateTime finDia = fecha.atTime(23, 59, 59);

            // Contar clientes creados en este día
            Long clientesNuevosDia = clienteRepository.findAll().stream()
                    .filter(c -> c.getFechaRegistro() != null && 
                                 c.getFechaRegistro().isAfter(inicioDia) && 
                                 c.getFechaRegistro().isBefore(finDia))
                    .count();

            tendencia.add(new TendenciaDiaria(
                fecha.format(formatter),
                clientesNuevosDia.doubleValue(),
                clientesNuevosDia
            ));
        }

        return tendencia;
    }
}
