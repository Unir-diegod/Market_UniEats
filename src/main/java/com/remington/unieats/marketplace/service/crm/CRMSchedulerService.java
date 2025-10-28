package com.remington.unieats.marketplace.service.crm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Servicio programado para tareas automáticas del CRM
 */
@Service
public class CRMSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(CRMSchedulerService.class);

    @Autowired
    private EmailMarketingService emailMarketingService;

    @Autowired
    private SegmentacionService segmentacionService;

    /**
     * Enviar emails pendientes cada 5 minutos
     */
    @Scheduled(fixedDelay = 300000) // 5 minutos
    public void enviarEmailsPendientes() {
        try {
            logger.info("Iniciando envío de emails pendientes...");
            emailMarketingService.enviarEmailsPendientes();
            logger.info("Envío de emails pendientes completado.");
        } catch (Exception e) {
            logger.error("Error al enviar emails pendientes: {}", e.getMessage());
        }
    }

    /**
     * Reintentar envíos fallidos cada hora
     */
    @Scheduled(fixedDelay = 3600000) // 1 hora
    public void reintentarEnviosFallidos() {
        try {
            logger.info("Reintentando envíos fallidos...");
            emailMarketingService.reintentarEnviosFallidos();
            logger.info("Reintento de envíos completado.");
        } catch (Exception e) {
            logger.error("Error al reintentar envíos fallidos: {}", e.getMessage());
        }
    }

    /**
     * Actualizar segmentos automáticos diariamente
     */
    @Scheduled(cron = "0 0 2 * * ?") // Todos los días a las 2 AM
    public void actualizarSegmentosAutomaticos() {
        try {
            logger.info("Actualizando segmentos automáticos...");
            
            // Actualizar segmentos predefinidos
            segmentacionService.segmentarClientesActivos();
            segmentacionService.segmentarClientesInactivos();
            segmentacionService.segmentarClientesFrecuentes(5);
            segmentacionService.segmentarClientesVIP(100000.0);
            
            // Actualizar segmentos por nivel
            segmentacionService.segmentarPorNivel("BRONCE");
            segmentacionService.segmentarPorNivel("PLATA");
            segmentacionService.segmentarPorNivel("ORO");
            segmentacionService.segmentarPorNivel("PLATINUM");
            
            logger.info("Segmentos automáticos actualizados.");
        } catch (Exception e) {
            logger.error("Error al actualizar segmentos: {}", e.getMessage());
        }
    }
}
