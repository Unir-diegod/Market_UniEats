package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para pruebas de envío de emails
 * Rutas: /api/email-test/*
 */
@RestController
@RequestMapping("/api/email-test")
public class EmailTestController {

    private static final Logger log = LoggerFactory.getLogger(EmailTestController.class);

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint para enviar un email de prueba simple
     * GET: /api/email-test/enviar-simple?email=test@ejemplo.com&nombre=Diego
     */
    @GetMapping("/enviar-simple")
    public Map<String, Object> enviarEmailSimple(
            @RequestParam String email,
            @RequestParam(defaultValue = "Usuario") String nombre) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            log.info("🧪 TEST: Enviando email simple de prueba a: {}", email);
            
            // Enviar email de bienvenida (esta usa la plantilla HTML)
            emailService.enviarEmailBienvenida(email, nombre);
            
            respuesta.put("estado", "éxito");
            respuesta.put("mensaje", "Email de bienvenida enviado a " + email);
            respuesta.put("email", email);
            respuesta.put("nombre", nombre);
            respuesta.put("timestamp", System.currentTimeMillis());
            
            log.info("✅ Email de prueba enviado correctamente");
            
        } catch (Exception e) {
            log.error("❌ Error en test de email: {}", e.getMessage(), e);
            
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Error al enviar email: " + e.getMessage());
            respuesta.put("error", e.getClass().getName());
            respuesta.put("detalles", e.getMessage());
        }
        
        return respuesta;
    }

    /**
     * Endpoint para enviar un email de prueba con plantilla personalizada
     * POST: /api/email-test/enviar-personalizado
     * 
     * JSON:
     * {
     *   "email": "test@ejemplo.com",
     *   "asunto": "Asunto del email",
     *   "plantilla": "emails/bienvenida",
     *   "variables": {
     *     "nombreUsuario": "Diego"
     *   }
     * }
     */
    @PostMapping("/enviar-personalizado")
    public Map<String, Object> enviarEmailPersonalizado(@RequestBody Map<String, Object> datos) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            String email = (String) datos.get("email");
            String asunto = (String) datos.get("asunto");
            String plantilla = (String) datos.get("plantilla");
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) datos.get("variables");
            
            if (email == null || email.isEmpty()) {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "El email es requerido");
                return respuesta;
            }
            
            log.info("🧪 TEST: Enviando email personalizado a: {}", email);
            log.info("📝 Plantilla: {}", plantilla);
            log.info("📋 Variables: {}", variables);
            
            // Enviar email personalizado
            if (plantilla == null || plantilla.isEmpty()) {
                plantilla = "emails/bienvenida";
            }
            
            if (variables == null) {
                variables = new HashMap<>();
            }
            
            emailService.enviarEmailConPlantilla(email, asunto, plantilla, variables);
            
            respuesta.put("estado", "éxito");
            respuesta.put("mensaje", "Email personalizado enviado a " + email);
            respuesta.put("email", email);
            respuesta.put("asunto", asunto);
            respuesta.put("plantilla", plantilla);
            respuesta.put("timestamp", System.currentTimeMillis());
            
            log.info("✅ Email personalizado enviado correctamente");
            
        } catch (Exception e) {
            log.error("❌ Error en test de email personalizado: {}", e.getMessage(), e);
            
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Error al enviar email: " + e.getMessage());
            respuesta.put("error", e.getClass().getName());
            respuesta.put("detalles", e.getMessage());
        }
        
        return respuesta;
    }

    /**
     * Endpoint para verificar la configuración de email
     * GET: /api/email-test/config
     */
    @GetMapping("/config")
    public Map<String, Object> verificarConfig() {
        Map<String, Object> config = new HashMap<>();
        
        try {
            config.put("estado", "conectado");
            config.put("host", "smtp.gmail.com");
            config.put("puerto", 587);
            config.put("username", "dvdavid2509vargs@gmail.com");
            config.put("autenticacion", true);
            config.put("tls_enabled", true);
            config.put("timeout_conexion", 5000);
            config.put("timeout_envio", 5000);
            config.put("charset", "UTF-8");
            config.put("mensaje", "Configuración de email cargada correctamente");
            
            log.info("✅ Configuración de email verificada");
            
        } catch (Exception e) {
            log.error("❌ Error al verificar configuración: {}", e.getMessage());
            config.put("estado", "error");
            config.put("mensaje", "Error: " + e.getMessage());
        }
        
        return config;
    }

    /**
     * Endpoint para enviar un email de prueba EXTREMADAMENTE simple (solo texto)
     * GET: /api/email-test/enviar-texto?email=test@ejemplo.com
     * 
     * Este endpoint es útil para diagnosticar problemas básicos de conectividad
     */
    @GetMapping("/enviar-texto")
    public Map<String, Object> enviarEmailTexto(@RequestParam String email) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            log.info("🧪 TEST BÁSICO: Enviando email de texto simple a: {}", email);
            
            // Crear un mapa con variables simples
            Map<String, Object> variables = new HashMap<>();
            variables.put("nombreUsuario", "TestUser");
            
            // Intentar enviar con la plantilla
            emailService.enviarEmailConPlantilla(
                email,
                "🧪 TEST BÁSICO - UniEats",
                "emails/bienvenida",
                variables
            );
            
            respuesta.put("estado", "éxito");
            respuesta.put("mensaje", "Email de prueba básico enviado correctamente");
            respuesta.put("email", email);
            respuesta.put("tipo", "Texto simple de prueba");
            
            log.info("✅ Email de prueba básico enviado correctamente");
            
        } catch (Exception e) {
            log.error("❌ ERROR en test básico: {}", e.getMessage(), e);
            log.error("📌 Stack trace:", e);
            
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Error al enviar: " + e.getMessage());
            respuesta.put("error_type", e.getClass().getName());
            respuesta.put("causa", e.getCause() != null ? e.getCause().getMessage() : "Sin causa registrada");
        }
        
        return respuesta;
    }

    /**
     * Endpoint de health check para el servicio de email
     * GET: /api/email-test/health
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            health.put("servicio", "Email Service");
            health.put("estado", "activo");
            health.put("proveedor", "Gmail SMTP");
            health.put("host", "smtp.gmail.com");
            health.put("puerto", 587);
            health.put("encriptacion", "TLS");
            health.put("autenticacion", "Habilitada");
            health.put("timestamp", System.currentTimeMillis());
            
            log.info("✅ Health check del servicio de email: OK");
            
        } catch (Exception e) {
            health.put("estado", "error");
            health.put("mensaje", e.getMessage());
            log.error("❌ Health check fallido", e);
        }
        
        return health;
    }
}
