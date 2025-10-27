package com.remington.unieats.marketplace.config;

import com.remington.unieats.marketplace.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler personalizado para manejar el éxito del login
 * Envía un email de bienvenida cada vez que el usuario inicia sesión
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private EmailService emailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        // Obtener información del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        log.info("✅ Login exitoso para usuario: {}", username);
        
        try {
            // Enviar email de bienvenida de forma asíncrona
            emailService.enviarEmailBienvenida(username, username);
            log.info("📧 Email de bienvenida programado para: {}", username);
        } catch (Exception e) {
            // No bloqueamos el login si falla el envío del email
            log.error("⚠️ Error al programar email de bienvenida (el login continúa): {}", e.getMessage());
        }
        
        // Redirigir al usuario a la página principal
        response.sendRedirect("/");
    }
}
