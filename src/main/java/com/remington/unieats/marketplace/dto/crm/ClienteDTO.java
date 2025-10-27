package com.remington.unieats.marketplace.dto.crm;

import java.time.LocalDateTime;

public class ClienteDTO {
    private Long id;
    private Integer usuarioId;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private Double valorTotalCompras;
    private Integer numeroPedidos;
    private Double ticketPromedio;
    private LocalDateTime ultimaCompra;
    private String frecuenciaCompra;
    private String categoriaFavorita;
    private Boolean aceptaMarketing;
    private Boolean aceptaEmail;
    private Boolean aceptaSms;
    private Integer puntosFidelidad;
    private String nivelCliente;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaInteraccion;

    // Constructores
    public ClienteDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getValorTotalCompras() {
        return valorTotalCompras;
    }

    public void setValorTotalCompras(Double valorTotalCompras) {
        this.valorTotalCompras = valorTotalCompras;
    }

    public Integer getNumeroPedidos() {
        return numeroPedidos;
    }

    public void setNumeroPedidos(Integer numeroPedidos) {
        this.numeroPedidos = numeroPedidos;
    }

    public Double getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(Double ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public LocalDateTime getUltimaCompra() {
        return ultimaCompra;
    }

    public void setUltimaCompra(LocalDateTime ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }

    public String getFrecuenciaCompra() {
        return frecuenciaCompra;
    }

    public void setFrecuenciaCompra(String frecuenciaCompra) {
        this.frecuenciaCompra = frecuenciaCompra;
    }

    public String getCategoriaFavorita() {
        return categoriaFavorita;
    }

    public void setCategoriaFavorita(String categoriaFavorita) {
        this.categoriaFavorita = categoriaFavorita;
    }

    public Boolean getAceptaMarketing() {
        return aceptaMarketing;
    }

    public void setAceptaMarketing(Boolean aceptaMarketing) {
        this.aceptaMarketing = aceptaMarketing;
    }

    public Boolean getAceptaEmail() {
        return aceptaEmail;
    }

    public void setAceptaEmail(Boolean aceptaEmail) {
        this.aceptaEmail = aceptaEmail;
    }

    public Boolean getAceptaSms() {
        return aceptaSms;
    }

    public void setAceptaSms(Boolean aceptaSms) {
        this.aceptaSms = aceptaSms;
    }

    public Integer getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(Integer puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }

    public String getNivelCliente() {
        return nivelCliente;
    }

    public void setNivelCliente(String nivelCliente) {
        this.nivelCliente = nivelCliente;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimaInteraccion() {
        return ultimaInteraccion;
    }

    public void setUltimaInteraccion(LocalDateTime ultimaInteraccion) {
        this.ultimaInteraccion = ultimaInteraccion;
    }
}
