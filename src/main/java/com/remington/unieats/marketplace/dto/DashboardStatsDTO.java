package com.remington.unieats.marketplace.dto;

import java.util.List;

public class DashboardStatsDTO {
    private long totalUsuarios;
    private long totalEstudiantes;
    private long totalVendedores;
    private long totalTiendas;
    private List<Long> pedidosPorDia; // Pedidos de los últimos 15 días

    // Getters y Setters
    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public long getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(long totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }
    public long getTotalVendedores() { return totalVendedores; }
    public void setTotalVendedores(long totalVendedores) { this.totalVendedores = totalVendedores; }
    public long getTotalTiendas() { return totalTiendas; }
    public void setTotalTiendas(long totalTiendas) { this.totalTiendas = totalTiendas; }
    public List<Long> getPedidosPorDia() { return pedidosPorDia; }
    public void setPedidosPorDia(List<Long> pedidosPorDia) { this.pedidosPorDia = pedidosPorDia; }
}

