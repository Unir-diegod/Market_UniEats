package com.remington.unieats.marketplace.model.entity;

/**
 * Enum que define los diferentes tipos de anuncios disponibles
 * en el sistema de personalización
 */
public enum TipoAnuncio {
    /**
     * Banner grande, típicamente en carrusel o header
     */
    BANNER,
    
    /**
     * Anuncio de un producto específico
     */
    PRODUCTO,
    
    /**
     * Anuncio de promoción o descuento
     */
    PROMOCION,
    
    /**
     * Anuncio destacando un vendedor/tienda
     */
    VENDEDOR,
    
    /**
     * Anuncio de una categoría completa de productos
     */
    CATEGORIA,
    
    /**
     * Notificación push (para futuras implementaciones)
     */
    PUSH_NOTIFICATION
}
