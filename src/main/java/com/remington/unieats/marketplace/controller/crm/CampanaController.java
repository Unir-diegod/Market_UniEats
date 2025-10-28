package com.remington.unieats.marketplace.controller.crm;

import com.remington.unieats.marketplace.dto.crm.CampanaDTO;
import com.remington.unieats.marketplace.dto.crm.CampanaRequest;
import com.remington.unieats.marketplace.model.entity.Campana;
import com.remington.unieats.marketplace.service.crm.CampanaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/crm/campanas")
@PreAuthorize("hasAnyRole('ADMIN', 'MARKETING')")
public class CampanaController {

    @Autowired
    private CampanaService campanaService;

    /**
     * Ver todas las campañas
     */
    @GetMapping
    public String listarCampanas(Model model) {
        List<CampanaDTO> campanas = campanaService.obtenerTodasCampanasDTO();
        model.addAttribute("campanas", campanas);
        return "crm/campanas/lista";
    }

    /**
     * Formulario para crear campaña
     */
    @GetMapping("/nueva")
    public String formularioNuevaCampana(Model model) {
        model.addAttribute("campanaRequest", new CampanaRequest());
        return "crm/campanas/formulario";
    }

    /**
     * Crear nueva campaña
     */
    @PostMapping("/crear")
    public String crearCampana(@Valid @ModelAttribute CampanaRequest request, 
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "crm/campanas/formulario";
        }

        try {
            Campana campana = campanaService.crearCampana(request);
            return "redirect:/crm/campanas/" + campana.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "crm/campanas/formulario";
        }
    }

    /**
     * Ver detalle de campaña
     */
    @GetMapping("/{id}")
    public String verCampana(@PathVariable Long id, Model model) {
        try {
            CampanaDTO campana = campanaService.convertirADTO(
                campanaService.obtenerCampanaPorId(id)
            );
            model.addAttribute("campana", campana);
            return "crm/campanas/detalle";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/crm/campanas";
        }
    }

    /**
     * Editar campaña
     */
    @GetMapping("/{id}/editar")
    public String formularioEditarCampana(@PathVariable Long id, Model model) {
        try {
            Campana campana = campanaService.obtenerCampanaPorId(id);
            model.addAttribute("campana", campana);
            return "crm/campanas/editar";
        } catch (Exception e) {
            return "redirect:/crm/campanas";
        }
    }

    /**
     * Actualizar campaña
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarCampana(@PathVariable Long id, 
                                    @Valid @ModelAttribute CampanaRequest request,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return "crm/campanas/editar";
        }

        try {
            campanaService.actualizarCampana(id, request);
            return "redirect:/crm/campanas/" + id;
        } catch (Exception e) {
            return "crm/campanas/editar";
        }
    }

    /**
     * API REST: Obtener todas las campañas
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<CampanaDTO>> obtenerCampanasAPI() {
        List<CampanaDTO> campanas = campanaService.obtenerTodasCampanasDTO();
        return ResponseEntity.ok(campanas);
    }

    /**
     * API REST: Activar campaña
     */
    @PostMapping("/api/{id}/activar")
    @ResponseBody
    public ResponseEntity<?> activarCampana(@PathVariable Long id) {
        try {
            Campana campana = campanaService.activarCampana(id);
            return ResponseEntity.ok(campanaService.convertirADTO(campana));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Pausar campaña
     */
    @PostMapping("/api/{id}/pausar")
    @ResponseBody
    public ResponseEntity<?> pausarCampana(@PathVariable Long id) {
        try {
            Campana campana = campanaService.pausarCampana(id);
            return ResponseEntity.ok(campanaService.convertirADTO(campana));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Finalizar campaña
     */
    @PostMapping("/api/{id}/finalizar")
    @ResponseBody
    public ResponseEntity<?> finalizarCampana(@PathVariable Long id) {
        try {
            Campana campana = campanaService.finalizarCampana(id);
            return ResponseEntity.ok(campanaService.convertirADTO(campana));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Eliminar campaña
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarCampana(@PathVariable Long id) {
        try {
            campanaService.eliminarCampana(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
