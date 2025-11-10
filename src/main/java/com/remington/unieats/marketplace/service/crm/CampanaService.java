package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.dto.crm.CampanaDTO;
import com.remington.unieats.marketplace.dto.crm.CampanaRequest;
import com.remington.unieats.marketplace.model.entity.Campana;
import com.remington.unieats.marketplace.model.entity.EmailTemplate;
import com.remington.unieats.marketplace.model.entity.Segmento;
import com.remington.unieats.marketplace.model.repository.CampanaRepository;
import com.remington.unieats.marketplace.model.repository.EmailTemplateRepository;
import com.remington.unieats.marketplace.model.repository.SegmentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    /**
     * Crear nueva campaña
     */
    @Transactional
    public Campana crearCampana(CampanaRequest request) {
        Segmento segmento = segmentoRepository.findById(request.getSegmentoId())
                .orElseThrow(() -> new RuntimeException("Segmento no encontrado"));

        Campana campana = new Campana();
        campana.setNombre(request.getNombre());
        campana.setDescripcion(request.getDescripcion());
        campana.setTipo(request.getTipo());
        campana.setEstado("BORRADOR");
        campana.setSegmento(segmento);
        campana.setFechaInicio(request.getFechaInicio());
        campana.setFechaFin(request.getFechaFin());
        campana.setPresupuesto(request.getPresupuesto());
        campana.setObjetivo(request.getObjetivo());
        campana.setFechaCreacion(LocalDateTime.now());

        if (request.getTemplateId() != null) {
            EmailTemplate template = emailTemplateRepository.findById(request.getTemplateId())
                    .orElse(null);
            campana.setTemplate(template);
        }

        return campanaRepository.save(campana);
    }

    /**
     * Actualizar campaña
     */
    @Transactional
    public Campana actualizarCampana(Long id, CampanaRequest request) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campana.setNombre(request.getNombre());
        campana.setDescripcion(request.getDescripcion());
        campana.setTipo(request.getTipo());
        campana.setFechaInicio(request.getFechaInicio());
        campana.setFechaFin(request.getFechaFin());
        campana.setPresupuesto(request.getPresupuesto());
        campana.setObjetivo(request.getObjetivo());

        if (request.getSegmentoId() != null) {
            Segmento segmento = segmentoRepository.findById(request.getSegmentoId())
                    .orElseThrow(() -> new RuntimeException("Segmento no encontrado"));
            campana.setSegmento(segmento);
        }

        if (request.getTemplateId() != null) {
            EmailTemplate template = emailTemplateRepository.findById(request.getTemplateId())
                    .orElse(null);
            campana.setTemplate(template);
        }

        return campanaRepository.save(campana);
    }

    /**
     * Activar campaña
     */
    @Transactional
    public Campana activarCampana(Long id) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campana.setEstado("ACTIVA");
        return campanaRepository.save(campana);
    }

    /**
     * Pausar campaña
     */
    @Transactional
    public Campana pausarCampana(Long id) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campana.setEstado("PAUSADA");
        return campanaRepository.save(campana);
    }

    /**
     * Finalizar campaña
     */
    @Transactional
    public Campana finalizarCampana(Long id) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campana.setEstado("FINALIZADA");
        return campanaRepository.save(campana);
    }

    /**
     * Actualizar métricas de campaña
     */
    @Transactional
    public Campana actualizarMetricas(Long id, Integer enviosExitosos, Integer enviosFallidos,
                                      Double tasaApertura, Double tasaClics, Integer conversiones,
                                      Double ingresosGenerados) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        if (enviosExitosos != null) {
            campana.setEnviosExitosos(campana.getEnviosExitosos() + enviosExitosos);
        }
        if (enviosFallidos != null) {
            campana.setEnviosFallidos(campana.getEnviosFallidos() + enviosFallidos);
        }
        campana.setEnviosTotales(campana.getEnviosExitosos() + campana.getEnviosFallidos());

        if (tasaApertura != null) campana.setTasaApertura(tasaApertura);
        if (tasaClics != null) campana.setTasaClics(tasaClics);
        if (conversiones != null) campana.setConversiones(conversiones);
        if (ingresosGenerados != null) {
            campana.setIngresosGenerados(ingresosGenerados);
            // Calcular ROI
            if (campana.getPresupuesto() != null && campana.getPresupuesto() > 0) {
                double roi = ((ingresosGenerados - campana.getPresupuesto()) / campana.getPresupuesto()) * 100;
                campana.setRoi(roi);
            }
        }

        return campanaRepository.save(campana);
    }

    /**
     * Obtener campañas activas
     */
    public List<Campana> obtenerCampanasActivas() {
        return campanaRepository.findCampanasActivas(LocalDateTime.now());
    }

    /**
     * Obtener campañas por estado
     */
    public List<Campana> obtenerCampanasPorEstado(String estado) {
        return campanaRepository.findByEstado(estado);
    }

    /**
     * Obtener campaña por ID
     */
    public Campana obtenerCampanaPorId(Long id) {
        return campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));
    }

    /**
     * Obtener todas las campañas
     */
    public List<Campana> obtenerTodasCampanas() {
        return campanaRepository.findAll();
    }

    /**
     * Obtener todos los segmentos disponibles para asociar a una campaña
     */
    public List<Segmento> obtenerTodosLosSegmentos() {
        return segmentoRepository.findAll();
    }

    /**
     * Obtener todas las plantillas de email disponibles
     */
    public List<EmailTemplate> obtenerTemplatesDisponibles() {
        return emailTemplateRepository.findAll();
    }

    /**
     * Convertir entidad a DTO
     */
    public CampanaDTO convertirADTO(Campana campana) {
        CampanaDTO dto = new CampanaDTO();
        dto.setId(campana.getId());
        dto.setNombre(campana.getNombre());
        dto.setDescripcion(campana.getDescripcion());
        dto.setTipo(campana.getTipo());
        dto.setEstado(campana.getEstado());
        
        if (campana.getSegmento() != null) {
            dto.setSegmentoId(campana.getSegmento().getId());
            dto.setSegmentoNombre(campana.getSegmento().getNombre());
        }
        
        if (campana.getTemplate() != null) {
            dto.setTemplateId(campana.getTemplate().getId());
            dto.setTemplateNombre(campana.getTemplate().getNombre());
        }
        
        dto.setFechaInicio(campana.getFechaInicio());
        dto.setFechaFin(campana.getFechaFin());
        dto.setFechaCreacion(campana.getFechaCreacion());
        dto.setPresupuesto(campana.getPresupuesto());
        dto.setObjetivo(campana.getObjetivo());
        dto.setEnviosTotales(campana.getEnviosTotales());
        dto.setEnviosExitosos(campana.getEnviosExitosos());
        dto.setEnviosFallidos(campana.getEnviosFallidos());
        dto.setTasaApertura(campana.getTasaApertura());
        dto.setTasaClics(campana.getTasaClics());
        dto.setConversiones(campana.getConversiones());
        dto.setIngresosGenerados(campana.getIngresosGenerados());
        dto.setRoi(campana.getRoi());
        
        return dto;
    }

    /**
     * Obtener todas las campañas como DTOs
     */
    public List<CampanaDTO> obtenerTodasCampanasDTO() {
        return campanaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a request para prellenar formularios
     */
    public CampanaRequest convertirARequest(Campana campana) {
        CampanaRequest request = new CampanaRequest();
        request.setNombre(campana.getNombre());
        request.setDescripcion(campana.getDescripcion());
        request.setTipo(campana.getTipo());
        request.setFechaInicio(campana.getFechaInicio());
        request.setFechaFin(campana.getFechaFin());
        request.setPresupuesto(campana.getPresupuesto());
        request.setObjetivo(campana.getObjetivo());

        if (campana.getSegmento() != null) {
            request.setSegmentoId(campana.getSegmento().getId());
        }

        if (campana.getTemplate() != null) {
            request.setTemplateId(campana.getTemplate().getId());
        }

        return request;
    }

    /**
     * Obtener total de ingresos generados
     */
    public Double obtenerTotalIngresosGenerados(LocalDateTime desde) {
        Double total = campanaRepository.getTotalIngresosGenerados(desde);
        return total != null ? total : 0.0;
    }

    /**
     * Eliminar campaña
     */
    @Transactional
    public void eliminarCampana(Long id) {
        campanaRepository.deleteById(id);
    }
}
