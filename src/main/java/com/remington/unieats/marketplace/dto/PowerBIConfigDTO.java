package com.remington.unieats.marketplace.dto;

/**
 * DTO para transferir configuración de Power BI al frontend
 */
public class PowerBIConfigDTO {

    private Long id;
    private Integer tiendaId;
    private String tiendaNombre;
    private String embedUrl;
    private String embedType; // PUBLIC o SERVICE
    private String accessToken; // Solo si embedType = SERVICE
    private Boolean activo;
    private Boolean tokenValido;
    private String configJson;

    // Información adicional
    private Boolean tieneConfiguracion;
    private String mensajeEstado;

    // Constructor vacío
    public PowerBIConfigDTO() {}

    // Constructor completo
    public PowerBIConfigDTO(Long id, Integer tiendaId, String tiendaNombre, String embedUrl, 
                           String embedType, String accessToken, Boolean activo, Boolean tokenValido, 
                           String configJson, Boolean tieneConfiguracion, String mensajeEstado) {
        this.id = id;
        this.tiendaId = tiendaId;
        this.tiendaNombre = tiendaNombre;
        this.embedUrl = embedUrl;
        this.embedType = embedType;
        this.accessToken = accessToken;
        this.activo = activo;
        this.tokenValido = tokenValido;
        this.configJson = configJson;
        this.tieneConfiguracion = tieneConfiguracion;
        this.mensajeEstado = mensajeEstado;
    }

    // Getters
    public Long getId() { return id; }
    public Integer getTiendaId() { return tiendaId; }
    public String getTiendaNombre() { return tiendaNombre; }
    public String getEmbedUrl() { return embedUrl; }
    public String getEmbedType() { return embedType; }
    public String getAccessToken() { return accessToken; }
    public Boolean getActivo() { return activo; }
    public Boolean getTokenValido() { return tokenValido; }
    public String getConfigJson() { return configJson; }
    public Boolean getTieneConfiguracion() { return tieneConfiguracion; }
    public String getMensajeEstado() { return mensajeEstado; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTiendaId(Integer tiendaId) { this.tiendaId = tiendaId; }
    public void setTiendaNombre(String tiendaNombre) { this.tiendaNombre = tiendaNombre; }
    public void setEmbedUrl(String embedUrl) { this.embedUrl = embedUrl; }
    public void setEmbedType(String embedType) { this.embedType = embedType; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setTokenValido(Boolean tokenValido) { this.tokenValido = tokenValido; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
    public void setTieneConfiguracion(Boolean tieneConfiguracion) { this.tieneConfiguracion = tieneConfiguracion; }
    public void setMensajeEstado(String mensajeEstado) { this.mensajeEstado = mensajeEstado; }
}
