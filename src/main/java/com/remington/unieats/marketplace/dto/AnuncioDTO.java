package com.remington.unieats.marketplace.dto;

/**
 * DTO para transferir información de anuncios personalizados al frontend
 */
public class AnuncioDTO {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String urlDestino;
    private String tipo;
    private Double scoreRelevancia;
    private String motivoPersonalizacion; // "Tu vendedor favorito", "Compraste esto antes", etc.
    
    // Información adicional según el tipo
    private Long productoId;
    private String productoNombre;
    private Double productoPrecio;
    
    private Long tiendaId;
    private String tiendaNombre;
    private String tiendaLogo;
    
    private String categoria;
    private Integer prioridad;
    
    // Constructor vacío
    public AnuncioDTO() {}
    
    // Constructor completo
    public AnuncioDTO(Long id, String titulo, String descripcion, String imagenUrl, 
                     String urlDestino, String tipo, Double scoreRelevancia, String motivoPersonalizacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.urlDestino = urlDestino;
        this.tipo = tipo;
        this.scoreRelevancia = scoreRelevancia;
        this.motivoPersonalizacion = motivoPersonalizacion;
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
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Double getScoreRelevancia() { return scoreRelevancia; }
    public void setScoreRelevancia(Double scoreRelevancia) { this.scoreRelevancia = scoreRelevancia; }
    
    public String getMotivoPersonalizacion() { return motivoPersonalizacion; }
    public void setMotivoPersonalizacion(String motivoPersonalizacion) { this.motivoPersonalizacion = motivoPersonalizacion; }
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    
    public Double getProductoPrecio() { return productoPrecio; }
    public void setProductoPrecio(Double productoPrecio) { this.productoPrecio = productoPrecio; }
    
    public Long getTiendaId() { return tiendaId; }
    public void setTiendaId(Long tiendaId) { this.tiendaId = tiendaId; }
    
    public String getTiendaNombre() { return tiendaNombre; }
    public void setTiendaNombre(String tiendaNombre) { this.tiendaNombre = tiendaNombre; }
    
    public String getTiendaLogo() { return tiendaLogo; }
    public void setTiendaLogo(String tiendaLogo) { this.tiendaLogo = tiendaLogo; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
}
