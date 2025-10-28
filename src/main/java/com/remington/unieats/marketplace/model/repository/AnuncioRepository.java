package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.Anuncio;
import com.remington.unieats.marketplace.model.entity.TipoAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar anuncios publicitarios
 */
@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
    
    /**
     * Encuentra anuncios activos por categorías
     */
    @Query("SELECT a FROM Anuncio a WHERE a.activo = true AND a.categoria IN :categorias " +
           "AND a.fechaInicio <= :ahora AND a.fechaFin >= :ahora")
    List<Anuncio> findActivosPorCategorias(@Param("categorias") List<String> categorias, 
                                           @Param("ahora") LocalDateTime ahora);
    
    /**
     * Encuentra anuncios activos de vendedores específicos
     */
    @Query("SELECT a FROM Anuncio a WHERE a.activo = true AND a.tienda.id IN :tiendaIds " +
           "AND a.fechaInicio <= :ahora AND a.fechaFin >= :ahora")
    List<Anuncio> findActivosPorVendedores(@Param("tiendaIds") List<Long> tiendaIds,
                                           @Param("ahora") LocalDateTime ahora);
    
    /**
     * Encuentra anuncios activos por tipo
     */
    @Query("SELECT a FROM Anuncio a WHERE a.tipo = :tipo AND a.activo = true " +
           "AND a.fechaInicio <= :ahora AND a.fechaFin >= :ahora")
    List<Anuncio> findByTipoAndActivoTrue(@Param("tipo") TipoAnuncio tipo,
                                          @Param("ahora") LocalDateTime ahora);
    
    /**
     * Encuentra anuncios vigentes en el rango de fechas
     */
    @Query("SELECT a FROM Anuncio a WHERE a.activo = true " +
           "AND a.fechaInicio <= :fechaFin AND a.fechaFin >= :fechaInicio")
    List<Anuncio> findByFechaInicioBeforeAndFechaFinAfter(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                          @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Encuentra todos los anuncios vigentes ahora
     */
    @Query("SELECT a FROM Anuncio a WHERE a.activo = true " +
           "AND a.fechaInicio <= :ahora AND a.fechaFin >= :ahora " +
           "ORDER BY a.prioridad DESC, a.fechaCreacion DESC")
    List<Anuncio> findAnunciosVigentes(@Param("ahora") LocalDateTime ahora);
    
    /**
     * Encuentra anuncios de una campaña específica
     */
    @Query("SELECT a FROM Anuncio a WHERE a.campana.id = :campanaId")
    List<Anuncio> findByCampanaId(@Param("campanaId") Long campanaId);
    
    /**
     * Encuentra anuncios de un producto específico
     */
    @Query("SELECT a FROM Anuncio a WHERE a.producto.id = :productoId AND a.activo = true " +
           "AND a.fechaInicio <= :ahora AND a.fechaFin >= :ahora")
    List<Anuncio> findByProductoIdAndActivo(@Param("productoId") Long productoId,
                                            @Param("ahora") LocalDateTime ahora);
    
    /**
     * Suma total de impresiones de todos los anuncios
     */
    @Query("SELECT COALESCE(SUM(a.impresiones), 0) FROM Anuncio a")
    Long sumImpresiones();
    
    /**
     * Suma total de clics de todos los anuncios
     */
    @Query("SELECT COALESCE(SUM(a.clics), 0) FROM Anuncio a")
    Long sumClics();
    
    /**
     * Suma total de conversiones de todos los anuncios
     */
    @Query("SELECT COALESCE(SUM(a.conversiones), 0) FROM Anuncio a")
    Long sumConversiones();
    
    /**
     * Calcula CTR promedio de anuncios activos
     */
    @Query("SELECT CASE WHEN SUM(a.impresiones) > 0 THEN " +
           "(CAST(SUM(a.clics) AS double) / CAST(SUM(a.impresiones) AS double)) * 100 " +
           "ELSE 0.0 END FROM Anuncio a WHERE a.activo = true")
    Double calcularCTRPromedio();
}
