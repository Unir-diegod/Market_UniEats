package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un anuncio publicitario en el sistema
 * Los anuncios pueden ser de diferentes tipos y estar vinculados a campañas CRM
 */
@Entity
@Table(name = "anuncios")
public class Anuncio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(length = 500)
    private String imagenUrl;
    
    @Column(length = 500)
    private String urlDestino;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnuncio tipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id")
    private Campana campana;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;
    
    @Column(length = 100)
    private String categoria;
    
    @Column(nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column(nullable = false)
    private LocalDateTime fechaFin;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(nullable = false)
    private Integer impresiones = 0;
    
    @Column(nullable = false)
    private Integer clics = 0;
    
    @Column(nullable = false)
    private Integer conversiones = 0;
    
    @Column(nullable = false)
    private Integer prioridad = 5;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    public Anuncio() {}
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) activo = true;
        if (impresiones == null) impresiones = 0;
        if (clics == null) clics = 0;
        if (conversiones == null) conversiones = 0;
        if (prioridad == null) prioridad = 5;
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    public Double calcularCTR() {
        if (impresiones == null || impresiones == 0) return 0.0;
        return (clics.doubleValue() / impresiones.doubleValue()) * 100;
    }
    
    public Double calcularTasaConversion() {
        if (clics == null || clics == 0) return 0.0;
        return (conversiones.doubleValue() / clics.doubleValue()) * 100;
    }
    
    public Boolean esVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return activo && !ahora.isBefore(fechaInicio) && !ahora.isAfter(fechaFin);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public String getUrlDestino() { return urlDestino; }
    public void setUrlDestino(String urlDestino) { this.urlDestino = urlDestino; }
    
    public TipoAnuncio getTipo() { return tipo; }
    public void setTipo(TipoAnuncio tipo) { this.tipo = tipo; }
    
    public Campana getCampana() { return campana; }
    public void setCampana(Campana campana) { this.campana = campana; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public Tienda getTienda() { return tienda; }
    public void setTienda(Tienda tienda) { this.tienda = tienda; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public Integer getImpresiones() { return impresiones; }
    public void setImpresiones(Integer impresiones) { this.impresiones = impresiones; }
    
    public Integer getClics() { return clics; }
    public void setClics(Integer clics) { this.clics = clics; }
    
    public Integer getConversiones() { return conversiones; }
    public void setConversiones(Integer conversiones) { this.conversiones = conversiones; }
    
    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
