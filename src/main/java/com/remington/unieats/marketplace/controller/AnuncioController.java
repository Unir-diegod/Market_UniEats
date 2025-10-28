package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.dto.AnuncioDTO;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.service.AnuncioPersonalizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de anuncios personalizados
 */
@RestController
@RequestMapping("/api/anuncios")
public class AnuncioController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnuncioController.class);
    
    @Autowired
    private AnuncioPersonalizacionService personalizacionService;
    
    /**
     * Obtiene anuncios personalizados para el usuario autenticado
     * @param limite Número máximo de anuncios (por defecto 10)
     * @return Lista de anuncios ordenados por relevancia
     */
    @GetMapping("/personalizados")
    public ResponseEntity<List<AnuncioDTO>> obtenerAnunciosPersonalizados(
            @RequestParam(defaultValue = "10") int limite) {
        
        try {
            Usuario usuario = obtenerUsuarioActual();
            if (usuario == null) {
                logger.warn("Usuario no autenticado intentando obtener anuncios");
                return ResponseEntity.status(401).build();
            }
            
            logger.info("Obteniendo {} anuncios personalizados para usuario: {}", 
                       limite, usuario.getNombre());
            
            List<AnuncioDTO> anuncios = personalizacionService.obtenerAnunciosPersonalizados(usuario, limite);
            
            return ResponseEntity.ok(anuncios);
            
        } catch (Exception e) {
            logger.error("Error obteniendo anuncios personalizados: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Registra una impresión (visualización) de un anuncio
     * @param id ID del anuncio
     * @param posicion Posición en la que se mostró (1 = primera posición)
     * @return 200 OK si se registró correctamente
     */
    @PostMapping("/{id}/impresion")
    public ResponseEntity<Void> registrarImpresion(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") Integer posicion) {
        
        try {
            Usuario usuario = obtenerUsuarioActual();
            if (usuario == null) {
                return ResponseEntity.status(401).build();
            }
            
            personalizacionService.registrarImpresion(id, usuario, posicion);
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error registrando impresión: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Registra un clic en un anuncio y actualiza el CTR
     * @param id ID del anuncio
     * @return 200 OK si se registró correctamente
     */
    @PostMapping("/{id}/clic")
    public ResponseEntity<Void> registrarClic(@PathVariable Long id) {
        
        try {
            Usuario usuario = obtenerUsuarioActual();
            if (usuario == null) {
                return ResponseEntity.status(401).build();
            }
            
            personalizacionService.registrarClic(id, usuario);
            
            logger.info("Clic registrado: anuncio {} por usuario {}", id, usuario.getNombre());
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error registrando clic: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Obtiene el usuario autenticado del contexto de seguridad
     */
    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Usuario) {
            return (Usuario) auth.getPrincipal();
        }
        return null;
    }
}
