package com.remington.unieats.marketplace.controller.crm;

import com.remington.unieats.marketplace.dto.crm.EstadisticasCRMDTO;
import com.remington.unieats.marketplace.dto.crm.DashboardCRMDTO;
import com.remington.unieats.marketplace.service.crm.EstadisticasCRMService;
import com.remington.unieats.marketplace.service.crm.DashboardCRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/crm/dashboard")
@PreAuthorize("hasAnyRole('ADMIN', 'MARKETING')")
public class DashboardCRMController {

    @Autowired
    private EstadisticasCRMService estadisticasService;

    @Autowired
    private DashboardCRMService dashboardService;

    /**
     * Dashboard principal de CRM
     */
    @GetMapping
    public String dashboardCRM(Model model) {
        // La vista cargará los datos dinámicamente vía API
        return "admin/admin_dashboard_crm";
    }

    /**
     * API REST: Obtener TODAS las métricas del Dashboard CRM
     * Este es el endpoint principal para el nuevo dashboard administrativo
     */
    @GetMapping("/api/metricas-completas")
    @ResponseBody
    public ResponseEntity<DashboardCRMDTO> obtenerMetricasCompletas() {
        DashboardCRMDTO metricas = dashboardService.obtenerMetricasDashboard();
        return ResponseEntity.ok(metricas);
    }

    /**
     * API REST: Obtener estadísticas generales (endpoint antiguo, mantener por compatibilidad)
     */
    @GetMapping("/api/estadisticas")
    @ResponseBody
    public ResponseEntity<EstadisticasCRMDTO> obtenerEstadisticas() {
        EstadisticasCRMDTO estadisticas = estadisticasService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * API REST: Obtener interacciones por tipo
     */
    @GetMapping("/api/interacciones-por-tipo")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> obtenerInteraccionesPorTipo(
            @RequestParam(required = false) Integer dias) {
        LocalDateTime desde = dias != null 
                ? LocalDateTime.now().minusDays(dias)
                : LocalDateTime.now().minusDays(30);
        
        Map<String, Long> stats = estadisticasService.obtenerInteraccionesPorTipo(desde);
        return ResponseEntity.ok(stats);
    }
}
