package com.remington.unieats.marketplace.dto.crm;

import java.time.LocalDateTime;

public class CampanaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String estado;
    private Long segmentoId;
    private String segmentoNombre;
    private Long templateId;
    private String templateNombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaCreacion;
    private Double presupuesto;
    private String objetivo;
    
    // Métricas
    private Integer enviosTotales;
    private Integer enviosExitosos;
    private Integer enviosFallidos;
    private Double tasaApertura;
    private Double tasaClics;
    private Integer conversiones;
    private Double ingresosGenerados;
    private Double roi;

    // Constructores
    public CampanaDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getSegmentoId() {
        return segmentoId;
    }

    public void setSegmentoId(Long segmentoId) {
        this.segmentoId = segmentoId;
    }

    public String getSegmentoNombre() {
        return segmentoNombre;
    }

    public void setSegmentoNombre(String segmentoNombre) {
        this.segmentoNombre = segmentoNombre;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateNombre() {
        return templateNombre;
    }

    public void setTemplateNombre(String templateNombre) {
        this.templateNombre = templateNombre;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Integer getEnviosTotales() {
        return enviosTotales;
    }

    public void setEnviosTotales(Integer enviosTotales) {
        this.enviosTotales = enviosTotales;
    }

    public Integer getEnviosExitosos() {
        return enviosExitosos;
    }

    public void setEnviosExitosos(Integer enviosExitosos) {
        this.enviosExitosos = enviosExitosos;
    }

    public Integer getEnviosFallidos() {
        return enviosFallidos;
    }

    public void setEnviosFallidos(Integer enviosFallidos) {
        this.enviosFallidos = enviosFallidos;
    }

    public Double getTasaApertura() {
        return tasaApertura;
    }

    public void setTasaApertura(Double tasaApertura) {
        this.tasaApertura = tasaApertura;
    }

    public Double getTasaClics() {
        return tasaClics;
    }

    public void setTasaClics(Double tasaClics) {
        this.tasaClics = tasaClics;
    }

    public Integer getConversiones() {
        return conversiones;
    }

    public void setConversiones(Integer conversiones) {
        this.conversiones = conversiones;
    }

    public Double getIngresosGenerados() {
        return ingresosGenerados;
    }

    public void setIngresosGenerados(Double ingresosGenerados) {
        this.ingresosGenerados = ingresosGenerados;
    }

    public Double getRoi() {
        return roi;
    }

    public void setRoi(Double roi) {
        this.roi = roi;
    }
}
