package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Segmento - Grupos de clientes para campañas dirigidas
 */
@Entity
@Table(name = "segmentos")
public class Segmento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_segmento")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "criterios", columnDefinition = "TEXT")
    private String criterios; // JSON con criterios de segmentación

    @Column(name = "cantidad_clientes")
    private Integer cantidadClientes = 0;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToMany
    @JoinTable(
        name = "segmento_clientes",
        joinColumns = @JoinColumn(name = "segmento_id"),
        inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private List<Cliente> clientes = new ArrayList<>();

    @OneToMany(mappedBy = "segmento")
    private List<Campana> campanas = new ArrayList<>();

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

    public String getCriterios() {
        return criterios;
    }

    public void setCriterios(String criterios) {
        this.criterios = criterios;
    }

    public Integer getCantidadClientes() {
        return cantidadClientes;
    }

    public void setCantidadClientes(Integer cantidadClientes) {
        this.cantidadClientes = cantidadClientes;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Campana> getCampanas() {
        return campanas;
    }

    public void setCampanas(List<Campana> campanas) {
        this.campanas = campanas;
    }
}
