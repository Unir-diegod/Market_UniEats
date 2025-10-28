package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que registra la relación entre un usuario y un anuncio
 * Permite tracking de impresiones, clics y relevancia personalizada
 */
@Entity
@Table(name = "anuncios_personalizados", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "anuncio_id", "fecha_mostrado"}),
       indexes = {
           @Index(name = "idx_usuario_id", columnList = "usuario_id"),
           @Index(name = "idx_anuncio_id", columnList = "anuncio_id"),
           @Index(name = "idx_fecha_mostrado", columnList = "fecha_mostrado")
       })
public class AnuncioPersonalizado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;
    
    @Column(nullable = false, name = "fecha_mostrado")
    private LocalDateTime fechaMostrado;
    
    @Column(nullable = false)
    private Boolean clickeado = false;
    
    private LocalDateTime fechaClic;
    
    @Column(nullable = false)
    private Double scoreRelevancia;
    
    @Column(nullable = false)
    private Boolean convertido = false;
    
    @Column(name = "pedido_id")
    private Long pedidoId;
    
    private Integer tiempoVisualizacion;
    
    private Integer posicion;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    public AnuncioPersonalizado() {}
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaMostrado == null) fechaMostrado = LocalDateTime.now();
        if (clickeado == null) clickeado = false;
        if (convertido == null) convertido = false;
    }
    
    public void registrarClic() {
        this.clickeado = true;
        this.fechaClic = LocalDateTime.now();
    }
    
    public void registrarConversion(Long pedidoId) {
        this.convertido = true;
        this.pedidoId = pedidoId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Anuncio getAnuncio() { return anuncio; }
    public void setAnuncio(Anuncio anuncio) { this.anuncio = anuncio; }
    
    public LocalDateTime getFechaMostrado() { return fechaMostrado; }
    public void setFechaMostrado(LocalDateTime fechaMostrado) { this.fechaMostrado = fechaMostrado; }
    
    public Boolean getClickeado() { return clickeado; }
    public void setClickeado(Boolean clickeado) { this.clickeado = clickeado; }
    
    public LocalDateTime getFechaClic() { return fechaClic; }
    public void setFechaClic(LocalDateTime fechaClic) { this.fechaClic = fechaClic; }
    
    public Double getScoreRelevancia() { return scoreRelevancia; }
    public void setScoreRelevancia(Double scoreRelevancia) { this.scoreRelevancia = scoreRelevancia; }
    
    public Boolean getConvertido() { return convertido; }
    public void setConvertido(Boolean convertido) { this.convertido = convertido; }
    
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    
    public Integer getTiempoVisualizacion() { return tiempoVisualizacion; }
    public void setTiempoVisualizacion(Integer tiempoVisualizacion) { this.tiempoVisualizacion = tiempoVisualizacion; }
    
    public Integer getPosicion() { return posicion; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
