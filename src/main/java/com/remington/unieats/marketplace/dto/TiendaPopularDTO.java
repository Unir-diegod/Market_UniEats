package com.remington.unieats.marketplace.dto;

public class TiendaPopularDTO {
    private String nombre;
    private Long cantidadPedidos;

    public TiendaPopularDTO(String nombre, Long cantidadPedidos) {
        this.nombre = nombre;
        this.cantidadPedidos = cantidadPedidos;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(Long cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }
}
