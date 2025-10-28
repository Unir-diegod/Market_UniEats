package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Campaña - Campañas de marketing
 */
@Entity
@Table(name = "campanas")
public class Campana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campana")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo; // EMAIL, SMS, PUSH, PROMOCION, DESCUENTO

    @Column(nullable = false, length = 50)
    private String estado = "BORRADOR"; // BORRADOR, PROGRAMADA, ACTIVA, PAUSADA, FINALIZADA

    @ManyToOne
    @JoinColumn(name = "segmento_id")
    private Segmento segmento;

    @OneToOne
    @JoinColumn(name = "template_id")
    private EmailTemplate template;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "presupuesto")
    private Double presupuesto;

    @Column(name = "objetivo", length = 500)
    private String objetivo;

    // Métricas de la campaña
    @Column(name = "envios_totales")
    private Integer enviosTotales = 0;

    @Column(name = "envios_exitosos")
    private Integer enviosExitosos = 0;

    @Column(name = "envios_fallidos")
    private Integer enviosFallidos = 0;

    @Column(name = "tasa_apertura")
    private Double tasaApertura = 0.0;

    @Column(name = "tasa_clics")
    private Double tasaClics = 0.0;

    @Column(name = "conversiones")
    private Integer conversiones = 0;

    @Column(name = "ingresos_generados")
    private Double ingresosGenerados = 0.0;

    @Column(name = "roi")
    private Double roi = 0.0;

    @OneToMany(mappedBy = "campana", cascade = CascadeType.ALL)
    private List<NotificacionMarketing> notificaciones = new ArrayList<>();

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

    public Segmento getSegmento() {
        return segmento;
    }

    public void setSegmento(Segmento segmento) {
        this.segmento = segmento;
    }

    public EmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EmailTemplate template) {
        this.template = template;
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

    public List<NotificacionMarketing> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<NotificacionMarketing> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
