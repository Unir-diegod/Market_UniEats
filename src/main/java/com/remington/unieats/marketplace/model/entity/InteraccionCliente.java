package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad InteraccionCliente - Registro de todas las interacciones con el cliente
 */
@Entity
@Table(name = "interacciones_cliente")
public class InteraccionCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interaccion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 50)
    private String tipo; // COMPRA, EMAIL_ABIERTO, EMAIL_CLIC, VISITA, REGISTRO, CARRITO_ABANDONADO

    @Column(nullable = false, length = 50)
    private String canal; // WEB, EMAIL, SMS, APP

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_interaccion", nullable = false)
    private LocalDateTime fechaInteraccion = LocalDateTime.now();

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON con datos adicionales

    @ManyToOne
    @JoinColumn(name = "campana_id")
    private Campana campana;

    @Column(name = "valor_monetario")
    private Double valorMonetario;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInteraccion() {
        return fechaInteraccion;
    }

    public void setFechaInteraccion(LocalDateTime fechaInteraccion) {
        this.fechaInteraccion = fechaInteraccion;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Campana getCampana() {
        return campana;
    }

    public void setCampana(Campana campana) {
        this.campana = campana;
    }

    public Double getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(Double valorMonetario) {
        this.valorMonetario = valorMonetario;
    }
}
