package com.remington.unieats.marketplace.model.enums;

public enum ClasificacionProducto {
    DESAYUNO("Desayuno"),
    ALMUERZO("Almuerzo"),
    COMIDA_RAPIDA("Comida Rápida"),
    BEBIDAS("Bebidas"),
    POSTRES("Postres"),
    SNACKS("Snacks"),
    SALUDABLE("Saludable"),
    SIN_CATEGORIA("Sin Categoría");

    private final String displayName;

    ClasificacionProducto(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}