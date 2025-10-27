package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Configuración de Power BI para cada tienda/vendedor
 * Permite almacenar la URL del reporte embebido y configuración relacionada
 */
@Entity
@Table(name = "powerbi_config")
public class PowerBIConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tienda a la que pertenece esta configuración
     */
    @OneToOne
    @JoinColumn(name = "tienda_id", nullable = false, unique = true)
    private Tienda tienda;

    /**
     * URL de embed del reporte de Power BI
     * Ejemplo: https://app.powerbi.com/view?r=xxxxx
     */
    @Column(name = "embed_url", length = 1000)
    private String embedUrl;

    /**
     * ID del Workspace de Power BI
     */
    @Column(name = "workspace_id", length = 100)
    private String workspaceId;

    /**
     * ID del Reporte de Power BI
     */
    @Column(name = "report_id", length = 100)
    private String reportId;

    /**
     * Token de acceso (si se usa autenticación de servicio)
     * Nota: Por seguridad, considerar encriptación
     */
    @Column(name = "access_token", length = 2000)
    private String accessToken;

    /**
     * Fecha de expiración del token
     */
    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    /**
     * Indica si el reporte está activo
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Tipo de embedding: PUBLIC (sin autenticación) o SERVICE (con token)
     */
    @Column(name = "embed_type", length = 20)
    private String embedType = "PUBLIC"; // PUBLIC o SERVICE

    /**
     * Configuración JSON adicional (filtros, temas, etc.)
     */
    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;

    /**
     * Fecha de creación
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha de última actualización
     */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public PowerBIConfig() {
    }

    public PowerBIConfig(Long id, Tienda tienda, String embedUrl, String workspaceId, String reportId, 
                        String accessToken, LocalDateTime tokenExpiry, Boolean activo, String embedType, 
                        String configJson, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.tienda = tienda;
        this.embedUrl = embedUrl;
        this.workspaceId = workspaceId;
        this.reportId = reportId;
        this.accessToken = accessToken;
        this.tokenExpiry = tokenExpiry;
        this.activo = activo;
        this.embedType = embedType;
        this.configJson = configJson;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEmbedType() {
        return embedType;
    }

    public void setEmbedType(String embedType) {
        this.embedType = embedType;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * Verifica si el token ha expirado
     */
    public boolean isTokenExpired() {
        if (tokenExpiry == null) return true;
        return LocalDateTime.now().isAfter(tokenExpiry);
    }

    /**
     * Verifica si la configuración es válida para mostrar el reporte
     */
    public boolean isValid() {
        return activo && embedUrl != null && !embedUrl.isEmpty();
    }
}
