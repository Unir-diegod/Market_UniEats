package com.remington.unieats.marketplace.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AnalyticsVendedorDTO {
    
    // KPIs principales
    private BigDecimal ventasTotales;
    private Long totalPedidos;
    private BigDecimal ventaPromedio;
    private Long totalProductos;
    
    // Datos para gráfica de ventas por día (últimos 30 días)
    private List<VentaDiariaDTO> ventasPorDia;
    
    // Productos más vendidos (top 5)
    private List<ProductoVendidoDTO> topProductos;
    
    // Distribución de pedidos por estado
    private Map<String, Long> pedidosPorEstado;
    
    // Tendencia de ventas
    private BigDecimal variacionVentas; // Porcentaje de cambio respecto al mes anterior
    
    // Constructors
    public AnalyticsVendedorDTO() {}
    
    public AnalyticsVendedorDTO(BigDecimal ventasTotales, Long totalPedidos, BigDecimal ventaPromedio, 
                                Long totalProductos, List<VentaDiariaDTO> ventasPorDia, 
                                List<ProductoVendidoDTO> topProductos, Map<String, Long> pedidosPorEstado, 
                                BigDecimal variacionVentas) {
        this.ventasTotales = ventasTotales;
        this.totalPedidos = totalPedidos;
        this.ventaPromedio = ventaPromedio;
        this.totalProductos = totalProductos;
        this.ventasPorDia = ventasPorDia;
        this.topProductos = topProductos;
        this.pedidosPorEstado = pedidosPorEstado;
        this.variacionVentas = variacionVentas;
    }
    
    // Getters and Setters
    public BigDecimal getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }
    
    public Long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }
    
    public BigDecimal getVentaPromedio() { return ventaPromedio; }
    public void setVentaPromedio(BigDecimal ventaPromedio) { this.ventaPromedio = ventaPromedio; }
    
    public Long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }
    
    public List<VentaDiariaDTO> getVentasPorDia() { return ventasPorDia; }
    public void setVentasPorDia(List<VentaDiariaDTO> ventasPorDia) { this.ventasPorDia = ventasPorDia; }
    
    public List<ProductoVendidoDTO> getTopProductos() { return topProductos; }
    public void setTopProductos(List<ProductoVendidoDTO> topProductos) { this.topProductos = topProductos; }
    
    public Map<String, Long> getPedidosPorEstado() { return pedidosPorEstado; }
    public void setPedidosPorEstado(Map<String, Long> pedidosPorEstado) { this.pedidosPorEstado = pedidosPorEstado; }
    
    public BigDecimal getVariacionVentas() { return variacionVentas; }
    public void setVariacionVentas(BigDecimal variacionVentas) { this.variacionVentas = variacionVentas; }
    
    // Inner DTOs
    public static class VentaDiariaDTO {
        private String fecha; // Formato: "2024-11-10"
        private BigDecimal ventas;
        private Long pedidos;
        
        public VentaDiariaDTO() {}
        
        public VentaDiariaDTO(String fecha, BigDecimal ventas, Long pedidos) {
            this.fecha = fecha;
            this.ventas = ventas;
            this.pedidos = pedidos;
        }
        
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        
        public BigDecimal getVentas() { return ventas; }
        public void setVentas(BigDecimal ventas) { this.ventas = ventas; }
        
        public Long getPedidos() { return pedidos; }
        public void setPedidos(Long pedidos) { this.pedidos = pedidos; }
    }
    
    public static class ProductoVendidoDTO {
        private String nombre;
        private Long cantidadVendida;
        private BigDecimal ingresos;
        
        public ProductoVendidoDTO() {}
        
        public ProductoVendidoDTO(String nombre, Long cantidadVendida, BigDecimal ingresos) {
            this.nombre = nombre;
            this.cantidadVendida = cantidadVendida;
            this.ingresos = ingresos;
        }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public Long getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
        
        public BigDecimal getIngresos() { return ingresos; }
        public void setIngresos(BigDecimal ingresos) { this.ingresos = ingresos; }
    }
}
