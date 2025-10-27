package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad NotificacionMarketing - Registro de notificaciones enviadas
 */
@Entity
@Table(name = "notificaciones_marketing")
public class NotificacionMarketing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campana_id", nullable = false)
    private Campana campana;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 50)
    private String tipo; // EMAIL, SMS, PUSH

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE"; // PENDIENTE, ENVIADO, ENTREGADO, FALLIDO, ABIERTO, CLIC

    @Column(name = "destinatario", nullable = false)
    private String destinatario; // Email o teléfono

    @Column(length = 200)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_clic")
    private LocalDateTime fechaClic;

    @Column(name = "error_mensaje", length = 500)
    private String errorMensaje;

    @Column(name = "intentos_envio")
    private Integer intentosEnvio = 0;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON con datos adicionales

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campana getCampana() {
        return campana;
    }

    public void setCampana(Campana campana) {
        this.campana = campana;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaClic() {
        return fechaClic;
    }

    public void setFechaClic(LocalDateTime fechaClic) {
        this.fechaClic = fechaClic;
    }

    public String getErrorMensaje() {
        return errorMensaje;
    }

    public void setErrorMensaje(String errorMensaje) {
        this.errorMensaje = errorMensaje;
    }

    public Integer getIntentosEnvio() {
        return intentosEnvio;
    }

    public void setIntentosEnvio(Integer intentosEnvio) {
        this.intentosEnvio = intentosEnvio;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
