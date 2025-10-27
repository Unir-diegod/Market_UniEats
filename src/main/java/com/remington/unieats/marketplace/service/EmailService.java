package com.remington.unieats.marketplace.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Servicio para el envío de correos electrónicos
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    private static final String FROM_EMAIL = "dvdavid2509vargs@gmail.com";
    private static final String FROM_NAME = "UniEats Marketplace";

    /**
     * Envía un email de bienvenida al usuario cuando se loguea
     */
    @Async
    public void enviarEmailBienvenida(String destinatario, String nombreUsuario) {
        try {
            log.info("📧 Preparando email de bienvenida para: {}", destinatario);
            
            // Crear contexto con variables para la plantilla
            Context context = new Context();
            context.setVariable("nombreUsuario", nombreUsuario);
            
            // Procesar la plantilla HTML
            String contenidoHtml = templateEngine.process("emails/bienvenida", context);
            
            // Enviar el email
            enviarEmailHtml(
                destinatario,
                "¡Bienvenido a UniEats! 🍔",
                contenidoHtml
            );
            
            log.info("✅ Email de bienvenida enviado exitosamente a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("❌ Error al enviar email de bienvenida a {}: {}", destinatario, e.getMessage(), e);
        }
    }

    /**
     * Envía un email HTML
     */
    private void enviarEmailHtml(String destinatario, String asunto, String contenidoHtml) throws MessagingException {
        try {
            log.info("🔧 Iniciando envío de email a: {}", destinatario);
            log.info("📧 Asunto: {}", asunto);
            
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            
            helper.setFrom(FROM_EMAIL, FROM_NAME);
            log.info("✉️ De: {} <{}>", FROM_NAME, FROM_EMAIL);
            
            helper.setTo(destinatario);
            log.info("➜ Para: {}", destinatario);
            
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true); // true = es HTML
            
            mailSender.send(mensaje);
            log.info("✅ Email enviado exitosamente a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("❌ ERROR AL ENVIAR EMAIL a {}", destinatario, e);
            log.error("📍 Tipo de error: {}", e.getClass().getName());
            log.error("📋 Mensaje de error: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("📌 Causa raíz: {}", e.getCause().getMessage());
            }
            throw new MessagingException("Error al enviar email a: " + destinatario, e);
        }
    }

    /**
     * Envía un email genérico con plantilla
     */
    @Async
    public void enviarEmailConPlantilla(String destinatario, String asunto, 
                                       String nombrePlantilla, Map<String, Object> variables) {
        try {
            log.info("📧 Enviando email a: {} con plantilla: {}", destinatario, nombrePlantilla);
            
            // Crear contexto con variables
            Context context = new Context();
            context.setVariables(variables);
            
            // Procesar la plantilla
            String contenidoHtml = templateEngine.process(nombrePlantilla, context);
            
            // Enviar el email
            enviarEmailHtml(destinatario, asunto, contenidoHtml);
            
            log.info("✅ Email enviado exitosamente a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("❌ Error al enviar email a {}: {}", destinatario, e.getMessage(), e);
        }
    }
}
