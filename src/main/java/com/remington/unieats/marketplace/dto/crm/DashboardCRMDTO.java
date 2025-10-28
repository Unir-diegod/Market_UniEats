package com.remington.unieats.marketplace.dto.crm;

import java.util.List;
import java.util.Map;

/**
 * DTO para Dashboard CRM del Administrador
 * Contiene todas las métricas consolidadas del CRM
 */
public class DashboardCRMDTO {

    // ========== MÉTRICAS GENERALES ==========
    private Long totalClientes;
    private Long clientesActivos;
    private Long clientesInactivos;
    private Long clientesNuevosUltimoMes;
    
    // ========== MÉTRICAS FINANCIERAS ==========
    private Double valorTotalVentas;
    private Double ticketPromedio;
    private Double ingresosUltimoMes;
    private Double crecimientoVentas; // Porcentaje vs mes anterior
    
    // ========== CAMPAÑAS ==========
    private Long totalCampanas;
    private Long campanasActivas;
    private Long campanasProgramadas;
    private Long campanasFinalizadas;
    private Map<String, Long> campanasPorEstado;
    
    // ========== EMAILS & MARKETING ==========
    private Integer emailsEnviadosTotal;
    private Integer emailsEnviadosUltimoMes;
    private Double tasaAperturaPromedio;
    private Double tasaClicsPromedio;
    private Integer conversionesEmail;
    
    // ========== SEGMENTACIÓN ==========
    private Long totalSegmentos;
    private Long segmentosActivos;
    private Map<String, Long> clientesPorSegmento;
    
    // ========== ROI & CONVERSIONES ==========
    private Double roiPromedio;
    private Integer conversionesTotales;
    private Double tasaConversionPromedio;
    private Double inversionTotalCampanas;
    private Double retornoTotalCampanas;
    
    // ========== TOP PRODUCTOS ==========
    private List<TopProductoDTO> topProductos;
    
    // ========== TOP VENDEDORES ==========
    private List<TopVendedorDTO> topVendedores;
    
    // ========== DISTRIBUCIÓN CLIENTES ==========
    private Map<String, Long> clientesPorNivel; // BRONCE, PLATA, ORO, PLATINUM
    
    // ========== INTERACCIONES ==========
    private Long totalInteracciones;
    private Map<String, Long> interaccionesPorTipo;
    private Map<String, Long> interaccionesPorCanal;
    
    // ========== TENDENCIAS (últimos 7 días) ==========
    private List<TendenciaDiaria> tendenciaVentas;
    private List<TendenciaDiaria> tendenciaClientes;

    // ========== Clases internas para datos complejos ==========
    
    public static class TopProductoDTO {
        private Long productoId;
        private String nombreProducto;
        private String categoria;
        private Long cantidadVendida;
        private Double ingresoTotal;
        private String vendedorNombre;

        // Constructors
        public TopProductoDTO() {}

        public TopProductoDTO(Long productoId, String nombreProducto, String categoria, 
                             Long cantidadVendida, Double ingresoTotal, String vendedorNombre) {
            this.productoId = productoId;
            this.nombreProducto = nombreProducto;
            this.categoria = categoria;
            this.cantidadVendida = cantidadVendida;
            this.ingresoTotal = ingresoTotal;
            this.vendedorNombre = vendedorNombre;
        }

        // Getters y Setters
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public Long getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
        
        public Double getIngresoTotal() { return ingresoTotal; }
        public void setIngresoTotal(Double ingresoTotal) { this.ingresoTotal = ingresoTotal; }
        
        public String getVendedorNombre() { return vendedorNombre; }
        public void setVendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; }
    }
    
    public static class TopVendedorDTO {
        private Long vendedorId;
        private String nombreVendedor;
        private String nombreNegocio;
        private Long totalPedidos;
        private Double ingresoTotal;
        private Double calificacionPromedio;
        private Long clientesUnicos;

        // Constructors
        public TopVendedorDTO() {}

        public TopVendedorDTO(Long vendedorId, String nombreVendedor, String nombreNegocio,
                             Long totalPedidos, Double ingresoTotal, Double calificacionPromedio,
                             Long clientesUnicos) {
            this.vendedorId = vendedorId;
            this.nombreVendedor = nombreVendedor;
            this.nombreNegocio = nombreNegocio;
            this.totalPedidos = totalPedidos;
            this.ingresoTotal = ingresoTotal;
            this.calificacionPromedio = calificacionPromedio;
            this.clientesUnicos = clientesUnicos;
        }

        // Getters y Setters
        public Long getVendedorId() { return vendedorId; }
        public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }
        
        public String getNombreVendedor() { return nombreVendedor; }
        public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }
        
        public String getNombreNegocio() { return nombreNegocio; }
        public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }
        
        public Long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }
        
        public Double getIngresoTotal() { return ingresoTotal; }
        public void setIngresoTotal(Double ingresoTotal) { this.ingresoTotal = ingresoTotal; }
        
        public Double getCalificacionPromedio() { return calificacionPromedio; }
        public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
        
        public Long getClientesUnicos() { return clientesUnicos; }
        public void setClientesUnicos(Long clientesUnicos) { this.clientesUnicos = clientesUnicos; }
    }
    
    public static class TendenciaDiaria {
        private String fecha;
        private Double valor;
        private Long cantidad;

        // Constructors
        public TendenciaDiaria() {}

        public TendenciaDiaria(String fecha, Double valor, Long cantidad) {
            this.fecha = fecha;
            this.valor = valor;
            this.cantidad = cantidad;
        }

        // Getters y Setters
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        
        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }
        
        public Long getCantidad() { return cantidad; }
        public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    }

    // ========== Getters y Setters principales ==========

    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }

    public Long getClientesActivos() { return clientesActivos; }
    public void setClientesActivos(Long clientesActivos) { this.clientesActivos = clientesActivos; }

    public Long getClientesInactivos() { return clientesInactivos; }
    public void setClientesInactivos(Long clientesInactivos) { this.clientesInactivos = clientesInactivos; }

    public Long getClientesNuevosUltimoMes() { return clientesNuevosUltimoMes; }
    public void setClientesNuevosUltimoMes(Long clientesNuevosUltimoMes) { this.clientesNuevosUltimoMes = clientesNuevosUltimoMes; }

    public Double getValorTotalVentas() { return valorTotalVentas; }
    public void setValorTotalVentas(Double valorTotalVentas) { this.valorTotalVentas = valorTotalVentas; }

    public Double getTicketPromedio() { return ticketPromedio; }
    public void setTicketPromedio(Double ticketPromedio) { this.ticketPromedio = ticketPromedio; }

    public Double getIngresosUltimoMes() { return ingresosUltimoMes; }
    public void setIngresosUltimoMes(Double ingresosUltimoMes) { this.ingresosUltimoMes = ingresosUltimoMes; }

    public Double getCrecimientoVentas() { return crecimientoVentas; }
    public void setCrecimientoVentas(Double crecimientoVentas) { this.crecimientoVentas = crecimientoVentas; }

    public Long getTotalCampanas() { return totalCampanas; }
    public void setTotalCampanas(Long totalCampanas) { this.totalCampanas = totalCampanas; }

    public Long getCampanasActivas() { return campanasActivas; }
    public void setCampanasActivas(Long campanasActivas) { this.campanasActivas = campanasActivas; }

    public Long getCampanasProgramadas() { return campanasProgramadas; }
    public void setCampanasProgramadas(Long campanasProgramadas) { this.campanasProgramadas = campanasProgramadas; }

    public Long getCampanasFinalizadas() { return campanasFinalizadas; }
    public void setCampanasFinalizadas(Long campanasFinalizadas) { this.campanasFinalizadas = campanasFinalizadas; }

    public Map<String, Long> getCampanasPorEstado() { return campanasPorEstado; }
    public void setCampanasPorEstado(Map<String, Long> campanasPorEstado) { this.campanasPorEstado = campanasPorEstado; }

    public Integer getEmailsEnviadosTotal() { return emailsEnviadosTotal; }
    public void setEmailsEnviadosTotal(Integer emailsEnviadosTotal) { this.emailsEnviadosTotal = emailsEnviadosTotal; }

    public Integer getEmailsEnviadosUltimoMes() { return emailsEnviadosUltimoMes; }
    public void setEmailsEnviadosUltimoMes(Integer emailsEnviadosUltimoMes) { this.emailsEnviadosUltimoMes = emailsEnviadosUltimoMes; }

    public Double getTasaAperturaPromedio() { return tasaAperturaPromedio; }
    public void setTasaAperturaPromedio(Double tasaAperturaPromedio) { this.tasaAperturaPromedio = tasaAperturaPromedio; }

    public Double getTasaClicsPromedio() { return tasaClicsPromedio; }
    public void setTasaClicsPromedio(Double tasaClicsPromedio) { this.tasaClicsPromedio = tasaClicsPromedio; }

    public Integer getConversionesEmail() { return conversionesEmail; }
    public void setConversionesEmail(Integer conversionesEmail) { this.conversionesEmail = conversionesEmail; }

    public Long getTotalSegmentos() { return totalSegmentos; }
    public void setTotalSegmentos(Long totalSegmentos) { this.totalSegmentos = totalSegmentos; }

    public Long getSegmentosActivos() { return segmentosActivos; }
    public void setSegmentosActivos(Long segmentosActivos) { this.segmentosActivos = segmentosActivos; }

    public Map<String, Long> getClientesPorSegmento() { return clientesPorSegmento; }
    public void setClientesPorSegmento(Map<String, Long> clientesPorSegmento) { this.clientesPorSegmento = clientesPorSegmento; }

    public Double getRoiPromedio() { return roiPromedio; }
    public void setRoiPromedio(Double roiPromedio) { this.roiPromedio = roiPromedio; }

    public Integer getConversionesTotales() { return conversionesTotales; }
    public void setConversionesTotales(Integer conversionesTotales) { this.conversionesTotales = conversionesTotales; }

    public Double getTasaConversionPromedio() { return tasaConversionPromedio; }
    public void setTasaConversionPromedio(Double tasaConversionPromedio) { this.tasaConversionPromedio = tasaConversionPromedio; }

    public Double getInversionTotalCampanas() { return inversionTotalCampanas; }
    public void setInversionTotalCampanas(Double inversionTotalCampanas) { this.inversionTotalCampanas = inversionTotalCampanas; }

    public Double getRetornoTotalCampanas() { return retornoTotalCampanas; }
    public void setRetornoTotalCampanas(Double retornoTotalCampanas) { this.retornoTotalCampanas = retornoTotalCampanas; }

    public List<TopProductoDTO> getTopProductos() { return topProductos; }
    public void setTopProductos(List<TopProductoDTO> topProductos) { this.topProductos = topProductos; }

    public List<TopVendedorDTO> getTopVendedores() { return topVendedores; }
    public void setTopVendedores(List<TopVendedorDTO> topVendedores) { this.topVendedores = topVendedores; }

    public Map<String, Long> getClientesPorNivel() { return clientesPorNivel; }
    public void setClientesPorNivel(Map<String, Long> clientesPorNivel) { this.clientesPorNivel = clientesPorNivel; }

    public Long getTotalInteracciones() { return totalInteracciones; }
    public void setTotalInteracciones(Long totalInteracciones) { this.totalInteracciones = totalInteracciones; }

    public Map<String, Long> getInteraccionesPorTipo() { return interaccionesPorTipo; }
    public void setInteraccionesPorTipo(Map<String, Long> interaccionesPorTipo) { this.interaccionesPorTipo = interaccionesPorTipo; }

    public Map<String, Long> getInteraccionesPorCanal() { return interaccionesPorCanal; }
    public void setInteraccionesPorCanal(Map<String, Long> interaccionesPorCanal) { this.interaccionesPorCanal = interaccionesPorCanal; }

    public List<TendenciaDiaria> getTendenciaVentas() { return tendenciaVentas; }
    public void setTendenciaVentas(List<TendenciaDiaria> tendenciaVentas) { this.tendenciaVentas = tendenciaVentas; }

    public List<TendenciaDiaria> getTendenciaClientes() { return tendenciaClientes; }
    public void setTendenciaClientes(List<TendenciaDiaria> tendenciaClientes) { this.tendenciaClientes = tendenciaClientes; }
}
