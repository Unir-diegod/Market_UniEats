package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.dto.PowerBIConfigDTO;
import com.remington.unieats.marketplace.model.entity.PowerBIConfig;
import com.remington.unieats.marketplace.model.entity.Tienda;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.PowerBIConfigRepository;
import com.remington.unieats.marketplace.model.repository.TiendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio para gestionar la integración con Power BI
 */
@Service
public class PowerBIService {

    private static final Logger logger = LoggerFactory.getLogger(PowerBIService.class);

    @Autowired
    private PowerBIConfigRepository powerBIConfigRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    /**
     * Obtener configuración de Power BI para un vendedor
     */
    @Transactional(readOnly = true)
    public PowerBIConfigDTO obtenerConfiguracionVendedor(Usuario vendedor) {
        try {
            Optional<Tienda> tiendaOpt = tiendaRepository.findByVendedor(vendedor);
            
            if (tiendaOpt.isEmpty()) {
                PowerBIConfigDTO dto = new PowerBIConfigDTO();
                dto.setTieneConfiguracion(false);
                dto.setMensajeEstado("No se encontró una tienda asociada");
                return dto;
            }

            Tienda tienda = tiendaOpt.get();
            Optional<PowerBIConfig> configOpt = powerBIConfigRepository.findByTiendaAndActivoTrue(tienda);

            if (configOpt.isEmpty()) {
                PowerBIConfigDTO dto = new PowerBIConfigDTO();
                dto.setTiendaId(tienda.getId());
                dto.setTiendaNombre(tienda.getNombre());
                dto.setTieneConfiguracion(false);
                dto.setMensajeEstado("No hay reporte de Power BI configurado");
                return dto;
            }

            PowerBIConfig config = configOpt.get();
            
            PowerBIConfigDTO dto = new PowerBIConfigDTO();
            dto.setId(config.getId());
            dto.setTiendaId(tienda.getId());
            dto.setTiendaNombre(tienda.getNombre());
            dto.setEmbedUrl(config.getEmbedUrl());
            dto.setEmbedType(config.getEmbedType());
            dto.setAccessToken(config.getAccessToken());
            dto.setActivo(config.getActivo());
            dto.setTokenValido(config.getTokenExpiry() != null && !config.isTokenExpired());
            dto.setConfigJson(config.getConfigJson());
            dto.setTieneConfiguracion(true);
            dto.setMensajeEstado("Reporte configurado correctamente");
            return dto;

        } catch (Exception e) {
            logger.error("Error obteniendo configuración de Power BI: {}", e.getMessage(), e);
            PowerBIConfigDTO dto = new PowerBIConfigDTO();
            dto.setTieneConfiguracion(false);
            dto.setMensajeEstado("Error al cargar la configuración");
            return dto;
        }
    }

    /**
     * Guardar o actualizar configuración de Power BI
     */
    @Transactional
    public PowerBIConfigDTO guardarConfiguracion(Usuario vendedor, PowerBIConfigDTO dto) {
        try {
            Optional<Tienda> tiendaOpt = tiendaRepository.findByVendedor(vendedor);
            
            if (tiendaOpt.isEmpty()) {
                throw new RuntimeException("No se encontró una tienda asociada al vendedor");
            }

            Tienda tienda = tiendaOpt.get();
            PowerBIConfig config;

            Optional<PowerBIConfig> existente = powerBIConfigRepository.findByTienda(tienda);
            
            if (existente.isPresent()) {
                config = existente.get();
                config.setEmbedUrl(dto.getEmbedUrl());
                config.setEmbedType(dto.getEmbedType());
                config.setAccessToken(dto.getAccessToken());
                config.setConfigJson(dto.getConfigJson());
                config.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
            } else {
                config = new PowerBIConfig();
                config.setTienda(tienda);
                config.setEmbedUrl(dto.getEmbedUrl());
                config.setEmbedType(dto.getEmbedType() != null ? dto.getEmbedType() : "PUBLIC");
                config.setAccessToken(dto.getAccessToken());
                config.setConfigJson(dto.getConfigJson());
                config.setActivo(true);
            }

            // Si se proporciona token, calcular expiración (por defecto 1 hora)
            if (dto.getAccessToken() != null && !dto.getAccessToken().isEmpty()) {
                config.setTokenExpiry(LocalDateTime.now().plusHours(1));
            }

            PowerBIConfig guardado = powerBIConfigRepository.save(config);
            logger.info("Configuración de Power BI guardada para tienda: {}", tienda.getNombre());

            PowerBIConfigDTO resultado = new PowerBIConfigDTO();
            resultado.setId(guardado.getId());
            resultado.setTiendaId(tienda.getId());
            resultado.setTiendaNombre(tienda.getNombre());
            resultado.setEmbedUrl(guardado.getEmbedUrl());
            resultado.setEmbedType(guardado.getEmbedType());
            resultado.setActivo(guardado.getActivo());
            resultado.setTieneConfiguracion(true);
            resultado.setMensajeEstado("Configuración guardada exitosamente");
            return resultado;

        } catch (Exception e) {
            logger.error("Error guardando configuración de Power BI: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la configuración: " + e.getMessage());
        }
    }

    /**
     * Eliminar configuración de Power BI
     */
    @Transactional
    public void eliminarConfiguracion(Usuario vendedor) {
        try {
            Optional<Tienda> tiendaOpt = tiendaRepository.findByVendedor(vendedor);
            
            if (tiendaOpt.isPresent()) {
                powerBIConfigRepository.deleteByTienda(tiendaOpt.get());
                logger.info("Configuración de Power BI eliminada para tienda: {}", tiendaOpt.get().getNombre());
            }
        } catch (Exception e) {
            logger.error("Error eliminando configuración de Power BI: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la configuración");
        }
    }

    /**
     * Activar/Desactivar configuración
     */
    @Transactional
    public void cambiarEstado(Usuario vendedor, boolean activo) {
        try {
            Optional<Tienda> tiendaOpt = tiendaRepository.findByVendedor(vendedor);
            
            if (tiendaOpt.isPresent()) {
                Optional<PowerBIConfig> configOpt = powerBIConfigRepository.findByTienda(tiendaOpt.get());
                
                if (configOpt.isPresent()) {
                    PowerBIConfig config = configOpt.get();
                    config.setActivo(activo);
                    powerBIConfigRepository.save(config);
                    logger.info("Estado de Power BI cambiado a {} para tienda: {}", 
                            activo ? "activo" : "inactivo", tiendaOpt.get().getNombre());
                }
            }
        } catch (Exception e) {
            logger.error("Error cambiando estado de Power BI: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cambiar el estado");
        }
    }
}
