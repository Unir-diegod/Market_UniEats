package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.dto.crm.EstadisticasCRMDTO;
import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.InteraccionCliente;
import com.remington.unieats.marketplace.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticasCRMService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private InteraccionClienteRepository interaccionRepository;

    /**
     * Obtener estadísticas generales del CRM
     */
    public EstadisticasCRMDTO obtenerEstadisticasGenerales() {
        EstadisticasCRMDTO stats = new EstadisticasCRMDTO();

        // Estadísticas de clientes
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        LocalDateTime hace60Dias = LocalDateTime.now().minusDays(60);

        stats.setTotalClientes(clienteRepository.count());
        stats.setClientesActivos((long) clienteRepository.findClientesActivosDesde(hace30Dias).size());
        stats.setClientesInactivos((long) clienteRepository.findClientesInactivos(hace60Dias).size());
        stats.setClientesNuevosUltimoMes(clienteRepository.countClientesNuevos(hace30Dias));

        // Estadísticas financieras
        List<Cliente> todosClientes = clienteRepository.findAll();
        double valorTotal = todosClientes.stream()
                .mapToDouble(Cliente::getValorTotalCompras)
                .sum();
        stats.setValorTotalVentas(valorTotal);
        stats.setTicketPromedioGeneral(clienteRepository.getTicketPromedioGeneral());

        // Estadísticas de campañas
        stats.setTotalCampanasActivas(campanaRepository.countByEstado("ACTIVA"));
        stats.setTotalSegmentos(segmentoRepository.countSegmentosActivos());

        // ROI promedio
        List<Object[]> campanasConRoi = campanaRepository.findAll().stream()
                .filter(c -> c.getRoi() != null && c.getRoi() > 0)
                .map(c -> new Object[]{c.getRoi()})
                .toList();
        
        if (!campanasConRoi.isEmpty()) {
            double roiPromedio = campanasConRoi.stream()
                    .mapToDouble(arr -> (Double) arr[0])
                    .average()
                    .orElse(0.0);
            stats.setRoiPromedio(roiPromedio);
        }

        // Distribución de clientes por nivel
        Map<String, Long> clientesPorNivel = new HashMap<>();
        clientesPorNivel.put("BRONCE", (long) clienteRepository.findByNivelCliente("BRONCE").size());
        clientesPorNivel.put("PLATA", (long) clienteRepository.findByNivelCliente("PLATA").size());
        clientesPorNivel.put("ORO", (long) clienteRepository.findByNivelCliente("ORO").size());
        clientesPorNivel.put("PLATINUM", (long) clienteRepository.findByNivelCliente("PLATINUM").size());
        stats.setClientesPorNivel(clientesPorNivel);

        // Distribución de campañas por estado
        Map<String, Long> campanasPorEstado = new HashMap<>();
        campanasPorEstado.put("BORRADOR", campanaRepository.countByEstado("BORRADOR"));
        campanasPorEstado.put("PROGRAMADA", campanaRepository.countByEstado("PROGRAMADA"));
        campanasPorEstado.put("ACTIVA", campanaRepository.countByEstado("ACTIVA"));
        campanasPorEstado.put("PAUSADA", campanaRepository.countByEstado("PAUSADA"));
        campanasPorEstado.put("FINALIZADA", campanaRepository.countByEstado("FINALIZADA"));
        stats.setCampanasPorEstado(campanasPorEstado);

        return stats;
    }

    /**
     * Registrar interacción del cliente
     */
    public InteraccionCliente registrarInteraccion(Long clienteId, String tipo, String canal, 
                                                   String descripcion, Double valorMonetario) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        InteraccionCliente interaccion = new InteraccionCliente();
        interaccion.setCliente(cliente);
        interaccion.setTipo(tipo);
        interaccion.setCanal(canal);
        interaccion.setDescripcion(descripcion);
        interaccion.setValorMonetario(valorMonetario);
        interaccion.setFechaInteraccion(LocalDateTime.now());

        // Actualizar última interacción del cliente
        cliente.setUltimaInteraccion(LocalDateTime.now());
        clienteRepository.save(cliente);

        return interaccionRepository.save(interaccion);
    }

    /**
     * Obtener historial de interacciones de un cliente
     */
    public List<InteraccionCliente> obtenerHistorialCliente(Long clienteId) {
        return interaccionRepository.findByClienteIdOrderByFechaDesc(clienteId);
    }

    /**
     * Obtener estadísticas de interacciones por tipo
     */
    public Map<String, Long> obtenerInteraccionesPorTipo(LocalDateTime desde) {
        List<Object[]> resultados = interaccionRepository.getInteraccionesPorTipo(desde);
        Map<String, Long> stats = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            String tipo = (String) resultado[0];
            Long cantidad = (Long) resultado[1];
            stats.put(tipo, cantidad);
        }
        
        return stats;
    }
}
