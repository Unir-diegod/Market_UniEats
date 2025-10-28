package com.remington.unieats.marketplace.controller.crm;

import com.remington.unieats.marketplace.dto.crm.SegmentoDTO;
import com.remington.unieats.marketplace.model.entity.Segmento;
import com.remington.unieats.marketplace.service.crm.SegmentacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/crm/segmentos")
@PreAuthorize("hasAnyRole('ADMIN', 'MARKETING')")
public class SegmentacionController {

    @Autowired
    private SegmentacionService segmentacionService;

    /**
     * Ver todos los segmentos
     */
    @GetMapping
    public String listarSegmentos(Model model) {
        List<SegmentoDTO> segmentos = segmentacionService.obtenerTodosSegmentosDTO();
        model.addAttribute("segmentos", segmentos);
        return "crm/segmentos/lista";
    }

    /**
     * Ver detalle de segmento
     */
    @GetMapping("/{id}")
    public String verSegmento(@PathVariable Long id, Model model) {
        try {
            Segmento segmento = segmentacionService.obtenerSegmentoPorId(id);
            model.addAttribute("segmento", segmentacionService.convertirADTO(segmento));
            model.addAttribute("clientes", segmento.getClientes());
            return "crm/segmentos/detalle";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/crm/segmentos";
        }
    }

    /**
     * Formulario para crear segmento
     */
    @GetMapping("/nuevo")
    public String formularioNuevoSegmento() {
        return "crm/segmentos/formulario";
    }

    /**
     * API REST: Obtener todos los segmentos
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<SegmentoDTO>> obtenerSegmentosAPI() {
        List<SegmentoDTO> segmentos = segmentacionService.obtenerTodosSegmentosDTO();
        return ResponseEntity.ok(segmentos);
    }

    /**
     * API REST: Crear segmento manual
     */
    @PostMapping("/api/crear")
    @ResponseBody
    public ResponseEntity<?> crearSegmento(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String criterios) {
        try {
            Segmento segmento = segmentacionService.crearSegmento(nombre, descripcion, criterios);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar clientes por nivel
     */
    @PostMapping("/api/segmentar-por-nivel")
    @ResponseBody
    public ResponseEntity<?> segmentarPorNivel(@RequestParam String nivel) {
        try {
            Segmento segmento = segmentacionService.segmentarPorNivel(nivel);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar clientes activos
     */
    @PostMapping("/api/segmentar-activos")
    @ResponseBody
    public ResponseEntity<?> segmentarActivos() {
        try {
            Segmento segmento = segmentacionService.segmentarClientesActivos();
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar clientes inactivos
     */
    @PostMapping("/api/segmentar-inactivos")
    @ResponseBody
    public ResponseEntity<?> segmentarInactivos() {
        try {
            Segmento segmento = segmentacionService.segmentarClientesInactivos();
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar clientes frecuentes
     */
    @PostMapping("/api/segmentar-frecuentes")
    @ResponseBody
    public ResponseEntity<?> segmentarFrecuentes(@RequestParam Integer minPedidos) {
        try {
            Segmento segmento = segmentacionService.segmentarClientesFrecuentes(minPedidos);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar clientes VIP
     */
    @PostMapping("/api/segmentar-vip")
    @ResponseBody
    public ResponseEntity<?> segmentarVIP(@RequestParam Double valorMinimo) {
        try {
            Segmento segmento = segmentacionService.segmentarClientesVIP(valorMinimo);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Segmentar por categoría
     */
    @PostMapping("/api/segmentar-por-categoria")
    @ResponseBody
    public ResponseEntity<?> segmentarPorCategoria(@RequestParam String categoria) {
        try {
            Segmento segmento = segmentacionService.segmentarPorCategoria(categoria);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Actualizar segmento
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> actualizarSegmento(
            @PathVariable Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String criterios) {
        try {
            Segmento segmento = segmentacionService.actualizarSegmento(id, nombre, descripcion, criterios);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Desactivar segmento
     */
    @PostMapping("/api/{id}/desactivar")
    @ResponseBody
    public ResponseEntity<?> desactivarSegmento(@PathVariable Long id) {
        try {
            Segmento segmento = segmentacionService.desactivarSegmento(id);
            return ResponseEntity.ok(segmentacionService.convertirADTO(segmento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Eliminar segmento
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarSegmento(@PathVariable Long id) {
        try {
            segmentacionService.eliminarSegmento(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
