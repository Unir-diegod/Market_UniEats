package com.remington.unieats.marketplace.controller.crm;

import com.remington.unieats.marketplace.dto.crm.ClienteDTO;
import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.InteraccionCliente;
import com.remington.unieats.marketplace.service.crm.ClienteService;
import com.remington.unieats.marketplace.service.crm.EstadisticasCRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crm/clientes")
@PreAuthorize("hasAnyRole('ADMIN', 'MARKETING')")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstadisticasCRMService estadisticasService;

    /**
     * Ver todos los clientes
     */
    @GetMapping
    public String listarClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.obtenerTodosClientesDTO();
        model.addAttribute("clientes", clientes);
        return "crm/clientes/lista";
    }

    /**
     * Ver detalle de cliente
     */
    @GetMapping("/{id}")
    public String verCliente(@PathVariable Long id, Model model) {
        try {
            Cliente cliente = clienteService.obtenerClientePorUsuarioId(id.intValue())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            ClienteDTO clienteDTO = clienteService.convertirADTO(cliente);
            List<InteraccionCliente> interacciones = estadisticasService.obtenerHistorialCliente(cliente.getId());
            
            model.addAttribute("cliente", clienteDTO);
            model.addAttribute("interacciones", interacciones);
            return "crm/clientes/detalle";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/crm/clientes";
        }
    }

    /**
     * API REST: Obtener clientes activos
     */
    @GetMapping("/api/activos")
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> obtenerClientesActivos() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesActivos()
                .stream()
                .map(clienteService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    /**
     * API REST: Obtener clientes inactivos
     */
    @GetMapping("/api/inactivos")
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> obtenerClientesInactivos() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesInactivos()
                .stream()
                .map(clienteService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    /**
     * API REST: Obtener top clientes
     */
    @GetMapping("/api/top")
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> obtenerTopClientes(
            @RequestParam(defaultValue = "100000") Double valorMinimo) {
        List<ClienteDTO> clientes = clienteService.obtenerTopClientes(valorMinimo)
                .stream()
                .map(clienteService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    /**
     * API REST: Actualizar preferencias de marketing
     */
    @PostMapping("/api/{id}/preferencias")
    @ResponseBody
    public ResponseEntity<?> actualizarPreferencias(
            @PathVariable Long id,
            @RequestParam Boolean aceptaMarketing,
            @RequestParam Boolean aceptaEmail,
            @RequestParam Boolean aceptaSms) {
        try {
            Cliente cliente = clienteService.actualizarPreferenciasMarketing(
                    id, aceptaMarketing, aceptaEmail, aceptaSms);
            return ResponseEntity.ok(clienteService.convertirADTO(cliente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API REST: Obtener historial de interacciones
     */
    @GetMapping("/api/{id}/interacciones")
    @ResponseBody
    public ResponseEntity<List<InteraccionCliente>> obtenerInteracciones(@PathVariable Long id) {
        List<InteraccionCliente> interacciones = estadisticasService.obtenerHistorialCliente(id);
        return ResponseEntity.ok(interacciones);
    }

    /**
     * API REST: Registrar interacción
     */
    @PostMapping("/api/{id}/interacciones")
    @ResponseBody
    public ResponseEntity<?> registrarInteraccion(
            @PathVariable Long id,
            @RequestParam String tipo,
            @RequestParam String canal,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Double valorMonetario) {
        try {
            InteraccionCliente interaccion = estadisticasService.registrarInteraccion(
                    id, tipo, canal, descripcion, valorMonetario);
            return ResponseEntity.ok(interaccion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
