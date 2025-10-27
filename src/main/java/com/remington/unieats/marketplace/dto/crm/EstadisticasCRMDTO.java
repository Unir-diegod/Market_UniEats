package com.remington.unieats.marketplace.dto.crm;

import java.util.Map;

public class EstadisticasCRMDTO {
    private Long totalClientes;
    private Long clientesActivos;
    private Long clientesInactivos;
    private Long clientesNuevosUltimoMes;
    private Double valorTotalVentas;
    private Double ticketPromedioGeneral;
    private Long totalCampanasActivas;
    private Long totalSegmentos;
    private Double tasaConversionPromedio;
    private Double roiPromedio;
    private Map<String, Long> clientesPorNivel;
    private Map<String, Long> campanasPorEstado;
    private Map<String, Double> ventasPorCategoria;

    // Constructores
    public EstadisticasCRMDTO() {}

    // Getters y Setters
    public Long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(Long totalClientes) {
        this.totalClientes = totalClientes;
    }

    public Long getClientesActivos() {
        return clientesActivos;
    }

    public void setClientesActivos(Long clientesActivos) {
        this.clientesActivos = clientesActivos;
    }

    public Long getClientesInactivos() {
        return clientesInactivos;
    }

    public void setClientesInactivos(Long clientesInactivos) {
        this.clientesInactivos = clientesInactivos;
    }

    public Long getClientesNuevosUltimoMes() {
        return clientesNuevosUltimoMes;
    }

    public void setClientesNuevosUltimoMes(Long clientesNuevosUltimoMes) {
        this.clientesNuevosUltimoMes = clientesNuevosUltimoMes;
    }

    public Double getValorTotalVentas() {
        return valorTotalVentas;
    }

    public void setValorTotalVentas(Double valorTotalVentas) {
        this.valorTotalVentas = valorTotalVentas;
    }

    public Double getTicketPromedioGeneral() {
        return ticketPromedioGeneral;
    }

    public void setTicketPromedioGeneral(Double ticketPromedioGeneral) {
        this.ticketPromedioGeneral = ticketPromedioGeneral;
    }

    public Long getTotalCampanasActivas() {
        return totalCampanasActivas;
    }

    public void setTotalCampanasActivas(Long totalCampanasActivas) {
        this.totalCampanasActivas = totalCampanasActivas;
    }

    public Long getTotalSegmentos() {
        return totalSegmentos;
    }

    public void setTotalSegmentos(Long totalSegmentos) {
        this.totalSegmentos = totalSegmentos;
    }

    public Double getTasaConversionPromedio() {
        return tasaConversionPromedio;
    }

    public void setTasaConversionPromedio(Double tasaConversionPromedio) {
        this.tasaConversionPromedio = tasaConversionPromedio;
    }

    public Double getRoiPromedio() {
        return roiPromedio;
    }

    public void setRoiPromedio(Double roiPromedio) {
        this.roiPromedio = roiPromedio;
    }

    public Map<String, Long> getClientesPorNivel() {
        return clientesPorNivel;
    }

    public void setClientesPorNivel(Map<String, Long> clientesPorNivel) {
        this.clientesPorNivel = clientesPorNivel;
    }

    public Map<String, Long> getCampanasPorEstado() {
        return campanasPorEstado;
    }

    public void setCampanasPorEstado(Map<String, Long> campanasPorEstado) {
        this.campanasPorEstado = campanasPorEstado;
    }

    public Map<String, Double> getVentasPorCategoria() {
        return ventasPorCategoria;
    }

    public void setVentasPorCategoria(Map<String, Double> ventasPorCategoria) {
        this.ventasPorCategoria = ventasPorCategoria;
    }
}
