package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cliente para CRM
 * Extiende la información del Usuario con datos específicos de marketing
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "valor_total_compras", nullable = false)
    private Double valorTotalCompras = 0.0;

    @Column(name = "numero_pedidos", nullable = false)
    private Integer numeroPedidos = 0;

    @Column(name = "ticket_promedio", nullable = false)
    private Double ticketPromedio = 0.0;

    @Column(name = "ultima_compra")
    private LocalDateTime ultimaCompra;

    @Column(name = "frecuencia_compra")
    private String frecuenciaCompra; // Diaria, Semanal, Quincenal, Mensual

    @Column(name = "categoria_favorita")
    private String categoriaFavorita;

    @Column(name = "acepta_marketing", nullable = false)
    private Boolean aceptaMarketing = true;

    @Column(name = "acepta_email", nullable = false)
    private Boolean aceptaEmail = true;

    @Column(name = "acepta_sms", nullable = false)
    private Boolean aceptaSms = false;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "ultima_interaccion")
    private LocalDateTime ultimaInteraccion;

    @Column(name = "puntos_fidelidad")
    private Integer puntosFidelidad = 0;

    @Column(name = "nivel_cliente")
    private String nivelCliente = "BRONCE"; // BRONCE, PLATA, ORO, PLATINUM

    @ManyToMany(mappedBy = "clientes")
    private List<Segmento> segmentos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<InteraccionCliente> interacciones = new ArrayList<>();

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public List<Segmento> getSegmentos() {
        return segmentos;
    }

    public void setSegmentos(List<Segmento> segmentos) {
        this.segmentos = segmentos;
    }

    public List<InteraccionCliente> getInteracciones() {
        return interacciones;
    }

    public void setInteracciones(List<InteraccionCliente> interacciones) {
        this.interacciones = interacciones;
    }
}
