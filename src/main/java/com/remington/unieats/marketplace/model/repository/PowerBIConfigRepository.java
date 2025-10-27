package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.PowerBIConfig;
import com.remington.unieats.marketplace.model.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar la configuración de Power BI
 */
@Repository
public interface PowerBIConfigRepository extends JpaRepository<PowerBIConfig, Long> {

    /**
     * Buscar configuración de Power BI por tienda
     */
    Optional<PowerBIConfig> findByTienda(Tienda tienda);

    /**
     * Buscar configuración activa por tienda
     */
    Optional<PowerBIConfig> findByTiendaAndActivoTrue(Tienda tienda);

    /**
     * Buscar configuración por ID de tienda
     */
    Optional<PowerBIConfig> findByTiendaId(Integer tiendaId);

    /**
     * Verificar si existe configuración para una tienda
     */
    boolean existsByTienda(Tienda tienda);

    /**
     * Eliminar configuración por tienda
     */
    void deleteByTienda(Tienda tienda);
}
