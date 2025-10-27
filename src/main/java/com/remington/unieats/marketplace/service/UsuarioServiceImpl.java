package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.dto.EstudianteRegistroDTO;
import com.remington.unieats.marketplace.model.entity.Rol;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.RolRepository;
import com.remington.unieats.marketplace.model.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    @Override
    public Usuario registrarEstudiante(EstudianteRegistroDTO registroDTO) {
        log.info("📝 Iniciando registro de nuevo usuario: {}", registroDTO.getCorreo());
        
        // Validar que el correo no exista
        if (usuarioRepository.findByCorreo(registroDTO.getCorreo()).isPresent()) {
            log.warn("⚠️ Intento de registro con email ya existente: {}", registroDTO.getCorreo());
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        // Crear el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setCedula(registroDTO.getCedula());
        usuario.setTelefono(registroDTO.getTelefono());
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setContrasenaHash(passwordEncoder.encode(registroDTO.getPassword()));

        // Asignar el rol de ESTUDIANTE
        Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE")
                .orElseThrow(() -> new RuntimeException("Error: El rol de ESTUDIANTE no se encuentra en la base de datos."));
        usuario.setRoles(Set.of(rolEstudiante));

        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("✅ Usuario registrado exitosamente: {} (ID: {})", usuario.getCorreo(), usuario.getId());
        
        // Enviar email de bienvenida de forma asíncrona
        try {
            log.info("📧 Preparando email de bienvenida para: {}", usuario.getCorreo());
            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
            emailService.enviarEmailBienvenida(usuario.getCorreo(), nombreCompleto);
            log.info("✅ Email de bienvenida programado para: {}", usuario.getCorreo());
        } catch (Exception e) {
            log.warn("⚠️ Error al enviar email de bienvenida a {} (el registro continuó): {}", usuario.getCorreo(), e.getMessage());
            // No lanzar excepción - el registro fue exitoso aunque falle el email
        }
        
        return usuarioGuardado;
    }
}