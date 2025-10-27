package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.model.repository.CampanaRepository;
import com.remington.unieats.marketplace.model.repository.ClienteRepository;
import com.remington.unieats.marketplace.model.repository.EmailTemplateRepository;
import com.remington.unieats.marketplace.model.repository.NotificacionMarketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailMarketingService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificacionMarketingRepository notificacionRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailTemplateRepository templateRepository;

    /**
     * Enviar campaña de email a un segmento
     */
    @Transactional
    public void enviarCampanaEmail(Long campanaId) {
        Campana campana = campanaRepository.findById(campanaId)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        if (!campana.getEstado().equals("ACTIVA")) {
            throw new RuntimeException("La campaña no está activa");
        }

        if (campana.getTemplate() == null) {
            throw new RuntimeException("La campaña no tiene un template asignado");
        }

        List<Cliente> clientes = campana.getSegmento().getClientes();
        
        for (Cliente cliente : clientes) {
            if (cliente.getAceptaEmail()) {
                programarEnvioEmail(campana, cliente);
            }
        }

        // Actualizar totales de la campaña
        campana.setEnviosTotales(clientes.size());
        campanaRepository.save(campana);
    }

    /**
     * Programar envío de email individual
     */
    @Transactional
    public NotificacionMarketing programarEnvioEmail(Campana campana, Cliente cliente) {
        NotificacionMarketing notificacion = new NotificacionMarketing();
        notificacion.setCampana(campana);
        notificacion.setCliente(cliente);
        notificacion.setTipo("EMAIL");
        notificacion.setEstado("PENDIENTE");
        notificacion.setDestinatario(cliente.getUsuario().getCorreo());
        
        EmailTemplate template = campana.getTemplate();
        notificacion.setAsunto(template.getAsunto());
        
        // Personalizar contenido
        String contenidoPersonalizado = personalizarContenido(
                template.getContenidoHtml(), 
                cliente
        );
        notificacion.setContenido(contenidoPersonalizado);
        
        notificacion.setFechaProgramada(LocalDateTime.now());
        notificacion.setIntentosEnvio(0);

        return notificacionRepository.save(notificacion);
    }

    /**
     * Enviar emails pendientes
     */
    @Transactional
    public void enviarEmailsPendientes() {
        List<NotificacionMarketing> pendientes = notificacionRepository
                .findNotificacionesPendientesPorEnviar(LocalDateTime.now());

        for (NotificacionMarketing notificacion : pendientes) {
            try {
                enviarEmailIndividual(notificacion);
                notificacion.setEstado("ENVIADO");
                notificacion.setFechaEnvio(LocalDateTime.now());
                
                // Actualizar métricas de campaña
                Campana campana = notificacion.getCampana();
                campana.setEnviosExitosos(campana.getEnviosExitosos() + 1);
                campanaRepository.save(campana);
                
            } catch (Exception e) {
                notificacion.setEstado("FALLIDO");
                notificacion.setErrorMensaje(e.getMessage());
                notificacion.setIntentosEnvio(notificacion.getIntentosEnvio() + 1);
                
                // Actualizar métricas de campaña
                Campana campana = notificacion.getCampana();
                campana.setEnviosFallidos(campana.getEnviosFallidos() + 1);
                campanaRepository.save(campana);
            }
            
            notificacionRepository.save(notificacion);
        }
    }

    /**
     * Enviar email individual
     */
    private void enviarEmailIndividual(NotificacionMarketing notificacion) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(notificacion.getDestinatario());
        helper.setSubject(notificacion.getAsunto());
        helper.setText(notificacion.getContenido(), true);
        helper.setFrom("noreply@unieats.com");

        mailSender.send(message);
    }

    /**
     * Personalizar contenido del email con datos del cliente
     */
    private String personalizarContenido(String contenido, Cliente cliente) {
        Usuario usuario = cliente.getUsuario();
        
        contenido = contenido.replace("{nombre}", usuario.getNombre());
        contenido = contenido.replace("{apellido}", usuario.getApellido());
        contenido = contenido.replace("{correo}", usuario.getCorreo());
        contenido = contenido.replace("{nivel}", cliente.getNivelCliente());
        contenido = contenido.replace("{puntos}", String.valueOf(cliente.getPuntosFidelidad()));
        
        return contenido;
    }

    /**
     * Registrar apertura de email
     */
    @Transactional
    public void registrarApertura(Long notificacionId) {
        NotificacionMarketing notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setEstado("ABIERTO");
        notificacion.setFechaApertura(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        // Actualizar métricas de campaña
        actualizarTasasApertura(notificacion.getCampana());
    }

    /**
     * Registrar clic en email
     */
    @Transactional
    public void registrarClic(Long notificacionId) {
        NotificacionMarketing notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setEstado("CLIC");
        notificacion.setFechaClic(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        // Actualizar métricas de campaña
        actualizarTasasClics(notificacion.getCampana());
    }

    /**
     * Actualizar tasa de apertura de campaña
     */
    private void actualizarTasasApertura(Campana campana) {
        Long abiertos = notificacionRepository.countAbiertosByCampanaId(campana.getId());
        if (campana.getEnviosExitosos() > 0) {
            double tasa = (abiertos * 100.0) / campana.getEnviosExitosos();
            campana.setTasaApertura(tasa);
            campanaRepository.save(campana);
        }
    }

    /**
     * Actualizar tasa de clics de campaña
     */
    private void actualizarTasasClics(Campana campana) {
        Long clics = notificacionRepository.countClicsByCampanaId(campana.getId());
        if (campana.getEnviosExitosos() > 0) {
            double tasa = (clics * 100.0) / campana.getEnviosExitosos();
            campana.setTasaClics(tasa);
            campanaRepository.save(campana);
        }
    }

    /**
     * Reintentar envíos fallidos
     */
    @Transactional
    public void reintentarEnviosFallidos() {
        List<NotificacionMarketing> fallidos = notificacionRepository.findNotificacionesParaReintentar();
        
        for (NotificacionMarketing notificacion : fallidos) {
            notificacion.setEstado("PENDIENTE");
            notificacion.setFechaProgramada(LocalDateTime.now());
            notificacionRepository.save(notificacion);
        }
    }

    /**
     * Crear template de email
     */
    @Transactional
    public EmailTemplate crearTemplate(String nombre, String asunto, String contenidoHtml, 
                                       String categoria, String descripcion) {
        EmailTemplate template = new EmailTemplate();
        template.setNombre(nombre);
        template.setAsunto(asunto);
        template.setContenidoHtml(contenidoHtml);
        template.setCategoria(categoria);
        template.setDescripcion(descripcion);
        template.setActivo(true);
        template.setFechaCreacion(LocalDateTime.now());

        return templateRepository.save(template);
    }

    /**
     * Obtener templates activos
     */
    public List<EmailTemplate> obtenerTemplatesActivos() {
        return templateRepository.findByActivoTrue();
    }

    /**
     * Obtener template por ID
     */
    public EmailTemplate obtenerTemplatePorId(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template no encontrado"));
    }
}
