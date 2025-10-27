package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampanaRepository extends JpaRepository<Campana, Long> {

    List<Campana> findByEstado(String estado);

    List<Campana> findByTipo(String tipo);

    @Query("SELECT c FROM Campana c WHERE c.fechaInicio <= :ahora AND c.fechaFin >= :ahora AND c.estado = 'ACTIVA'")
    List<Campana> findCampanasActivas(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT c FROM Campana c WHERE c.fechaFin < :ahora AND c.estado != 'FINALIZADA'")
    List<Campana> findCampanasExpiradas(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT c FROM Campana c WHERE c.fechaInicio <= :ahora AND c.estado = 'PROGRAMADA'")
    List<Campana> findCampanasPorActivar(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT c FROM Campana c WHERE c.segmento.id = :segmentoId")
    List<Campana> findBySegmentoId(@Param("segmentoId") Long segmentoId);

    @Query("SELECT c FROM Campana c ORDER BY c.roi DESC")
    List<Campana> findCampanasOrderByRoi();

    @Query("SELECT c FROM Campana c WHERE c.conversiones > 0 ORDER BY c.conversiones DESC")
    List<Campana> findCampanasConConversiones();

    @Query("SELECT SUM(c.ingresosGenerados) FROM Campana c WHERE c.fechaCreacion >= :fechaInicio")
    Double getTotalIngresosGenerados(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT COUNT(c) FROM Campana c WHERE c.estado = :estado")
    Long countByEstado(@Param("estado") String estado);
}
