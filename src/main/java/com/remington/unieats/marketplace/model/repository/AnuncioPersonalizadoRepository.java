package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.AnuncioPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar el historial de anuncios personalizados por usuario
 */
@Repository
public interface AnuncioPersonalizadoRepository extends JpaRepository<AnuncioPersonalizado, Long> {
    
    /**
     * Encuentra todos los anuncios mostrados a un usuario
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "ORDER BY ap.fechaMostrado DESC")
    List<AnuncioPersonalizado> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Encuentra anuncios mostrados a un usuario en un rango de fechas
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.fechaMostrado BETWEEN :desde AND :hasta")
    List<AnuncioPersonalizado> findByUsuarioIdAndFechaMostradoBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
    
    /**
     * Encuentra anuncios en los que el usuario hizo clic
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.clickeado = true ORDER BY ap.fechaClic DESC")
    List<AnuncioPersonalizado> findByUsuarioIdAndClickeadoTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Encuentra anuncios que generaron conversiones para un usuario
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.convertido = true ORDER BY ap.fechaCreacion DESC")
    List<AnuncioPersonalizado> findByUsuarioIdAndConvertidoTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Verifica si un usuario ya vio un anuncio específico
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.anuncio.id = :anuncioId")
    List<AnuncioPersonalizado> findByUsuarioIdAndAnuncioId(
            @Param("usuarioId") Long usuarioId,
            @Param("anuncioId") Long anuncioId);
    
    /**
     * Cuenta cuántas veces un usuario vio un anuncio
     */
    @Query("SELECT COUNT(ap) FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.anuncio.id = :anuncioId")
    Long countByUsuarioIdAndAnuncioId(@Param("usuarioId") Long usuarioId, 
                                      @Param("anuncioId") Long anuncioId);
    
    /**
     * Encuentra el último anuncio mostrado de cada tipo para un usuario
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "AND ap.anuncio.tipo = :tipo ORDER BY ap.fechaMostrado DESC")
    List<AnuncioPersonalizado> findTopByUsuarioIdAndTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") String tipo);
    
    /**
     * Calcula el CTR personal de un usuario (clics / impresiones)
     */
    @Query("SELECT CASE WHEN COUNT(ap) > 0 THEN " +
           "(CAST(SUM(CASE WHEN ap.clickeado = true THEN 1 ELSE 0 END) AS double) / CAST(COUNT(ap) AS double)) * 100 " +
           "ELSE 0.0 END FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId")
    Double calcularCTRPersonal(@Param("usuarioId") Long usuarioId);
    
    /**
     * Obtiene el score promedio de relevancia de anuncios mostrados a un usuario
     */
    @Query("SELECT AVG(ap.scoreRelevancia) FROM AnuncioPersonalizado ap " +
           "WHERE ap.usuario.id = :usuarioId")
    Double calcularScorePromedioPersonal(@Param("usuarioId") Long usuarioId);
    
    /**
     * Cuenta anuncios únicos vistos por un usuario
     */
    @Query("SELECT COUNT(DISTINCT ap.anuncio.id) FROM AnuncioPersonalizado ap " +
           "WHERE ap.usuario.id = :usuarioId")
    Long countAnunciosUnicosVistos(@Param("usuarioId") Long usuarioId);
    
    /**
     * Obtiene estadísticas de un anuncio específico
     */
    @Query("SELECT COUNT(ap), " +
           "SUM(CASE WHEN ap.clickeado = true THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN ap.convertido = true THEN 1 ELSE 0 END) " +
           "FROM AnuncioPersonalizado ap WHERE ap.anuncio.id = :anuncioId")
    List<Object[]> obtenerEstadisticasAnuncio(@Param("anuncioId") Long anuncioId);
    
    /**
     * Encuentra anuncios con mejor score para un usuario (para análisis)
     */
    @Query("SELECT ap FROM AnuncioPersonalizado ap WHERE ap.usuario.id = :usuarioId " +
           "ORDER BY ap.scoreRelevancia DESC")
    List<AnuncioPersonalizado> findTopByScoreRelevancia(@Param("usuarioId") Long usuarioId);
}
