package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.dto.PowerBIConfigDTO;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.service.PowerBIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gestionar la integración con Power BI
 */
@RestController
@RequestMapping("/api/powerbi")
@PreAuthorize("hasRole('VENDEDOR')")
public class PowerBIController {

    @Autowired
    private PowerBIService powerBIService;

    /**
     * Obtener configuración de Power BI del vendedor actual
     */
    @GetMapping("/config")
    public ResponseEntity<PowerBIConfigDTO> obtenerConfiguracion(
            @AuthenticationPrincipal Usuario vendedor) {
        PowerBIConfigDTO config = powerBIService.obtenerConfiguracionVendedor(vendedor);
        return ResponseEntity.ok(config);
    }

    /**
     * Guardar o actualizar configuración de Power BI
     */
    @PostMapping("/config")
    public ResponseEntity<PowerBIConfigDTO> guardarConfiguracion(
            @AuthenticationPrincipal Usuario vendedor,
            @RequestBody PowerBIConfigDTO dto) {
        PowerBIConfigDTO resultado = powerBIService.guardarConfiguracion(vendedor, dto);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Eliminar configuración de Power BI
     */
    @DeleteMapping("/config")
    public ResponseEntity<Void> eliminarConfiguracion(
            @AuthenticationPrincipal Usuario vendedor) {
        powerBIService.eliminarConfiguracion(vendedor);
        return ResponseEntity.ok().build();
    }

    /**
     * Activar/Desactivar reporte de Power BI
     */
    @PatchMapping("/config/estado")
    public ResponseEntity<Void> cambiarEstado(
            @AuthenticationPrincipal Usuario vendedor,
            @RequestParam boolean activo) {
        powerBIService.cambiarEstado(vendedor, activo);
        return ResponseEntity.ok().build();
    }
}
