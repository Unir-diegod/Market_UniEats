package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.dto.crm.SegmentoDTO;
import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.Segmento;
import com.remington.unieats.marketplace.model.repository.ClienteRepository;
import com.remington.unieats.marketplace.model.repository.SegmentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SegmentacionService {

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Crear nuevo segmento
     */
    @Transactional
    public Segmento crearSegmento(String nombre, String descripcion, String criterios) {
        Segmento segmento = new Segmento();
        segmento.setNombre(nombre);
        segmento.setDescripcion(descripcion);
        segmento.setCriterios(criterios);
        segmento.setFechaCreacion(LocalDateTime.now());
        segmento.setActivo(true);
        segmento.setCantidadClientes(0);

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar clientes por nivel
     */
    @Transactional
    public Segmento segmentarPorNivel(String nivel) {
        String nombreSegmento = "Clientes " + nivel;
        
        // Verificar si ya existe
        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Segmento automático de clientes nivel " + nivel, 
                        "{\"nivel\": \"" + nivel + "\"}"));

        List<Cliente> clientes = clienteRepository.findByNivelCliente(nivel);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar clientes activos (compraron en últimos 30 días)
     */
    @Transactional
    public Segmento segmentarClientesActivos() {
        String nombreSegmento = "Clientes Activos";
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);

        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Clientes con compras en los últimos 30 días", 
                        "{\"ultima_compra\": \"30_dias\"}"));

        List<Cliente> clientes = clienteRepository.findClientesActivosDesde(hace30Dias);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar clientes inactivos (sin compras en últimos 60 días)
     */
    @Transactional
    public Segmento segmentarClientesInactivos() {
        String nombreSegmento = "Clientes Inactivos";
        LocalDateTime hace60Dias = LocalDateTime.now().minusDays(60);

        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Clientes sin compras en los últimos 60 días", 
                        "{\"ultima_compra\": \">60_dias\"}"));

        List<Cliente> clientes = clienteRepository.findClientesInactivos(hace60Dias);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar clientes frecuentes (más de X pedidos)
     */
    @Transactional
    public Segmento segmentarClientesFrecuentes(Integer minPedidos) {
        String nombreSegmento = "Clientes Frecuentes";

        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Clientes con " + minPedidos + " o más pedidos", 
                        "{\"min_pedidos\": " + minPedidos + "}"));

        List<Cliente> clientes = clienteRepository.findClientesFrecuentes(minPedidos);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar clientes VIP (top valor de compras)
     */
    @Transactional
    public Segmento segmentarClientesVIP(Double valorMinimo) {
        String nombreSegmento = "Clientes VIP";

        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Clientes con valor total de compras superior a $" + valorMinimo, 
                        "{\"valor_minimo\": " + valorMinimo + "}"));

        List<Cliente> clientes = clienteRepository.findTopClientesByValorCompras(valorMinimo);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Segmentar por categoría favorita
     */
    @Transactional
    public Segmento segmentarPorCategoria(String categoria) {
        String nombreSegmento = "Amantes de " + categoria;

        Segmento segmento = segmentoRepository.findByNombre(nombreSegmento)
                .orElse(crearSegmento(nombreSegmento, 
                        "Clientes con categoría favorita: " + categoria, 
                        "{\"categoria_favorita\": \"" + categoria + "\"}"));

        List<Cliente> clientes = clienteRepository.findByCategoriaFavorita(categoria);
        segmento.setClientes(clientes);
        segmento.setCantidadClientes(clientes.size());
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Obtener segmento por ID
     */
    public Segmento obtenerSegmentoPorId(Long id) {
        return segmentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segmento no encontrado"));
    }

    /**
     * Obtener todos los segmentos activos
     */
    public List<Segmento> obtenerSegmentosActivos() {
        return segmentoRepository.findByActivoTrue();
    }

    /**
     * Actualizar segmento
     */
    @Transactional
    public Segmento actualizarSegmento(Long id, String nombre, String descripcion, String criterios) {
        Segmento segmento = obtenerSegmentoPorId(id);
        
        if (nombre != null) segmento.setNombre(nombre);
        if (descripcion != null) segmento.setDescripcion(descripcion);
        if (criterios != null) segmento.setCriterios(criterios);
        
        segmento.setFechaActualizacion(LocalDateTime.now());

        return segmentoRepository.save(segmento);
    }

    /**
     * Desactivar segmento
     */
    @Transactional
    public Segmento desactivarSegmento(Long id) {
        Segmento segmento = obtenerSegmentoPorId(id);
        segmento.setActivo(false);
        return segmentoRepository.save(segmento);
    }

    /**
     * Convertir entidad a DTO
     */
    public SegmentoDTO convertirADTO(Segmento segmento) {
        SegmentoDTO dto = new SegmentoDTO();
        dto.setId(segmento.getId());
        dto.setNombre(segmento.getNombre());
        dto.setDescripcion(segmento.getDescripcion());
        dto.setCriterios(segmento.getCriterios());
        dto.setCantidadClientes(segmento.getCantidadClientes());
        dto.setFechaCreacion(segmento.getFechaCreacion());
        dto.setFechaActualizacion(segmento.getFechaActualizacion());
        dto.setActivo(segmento.getActivo());
        return dto;
    }

    /**
     * Obtener todos los segmentos como DTOs
     */
    public List<SegmentoDTO> obtenerTodosSegmentosDTO() {
        return segmentoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar segmento
     */
    @Transactional
    public void eliminarSegmento(Long id) {
        segmentoRepository.deleteById(id);
    }
}
