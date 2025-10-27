package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.NotificacionMarketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionMarketingRepository extends JpaRepository<NotificacionMarketing, Long> {

    List<NotificacionMarketing> findByEstado(String estado);

    List<NotificacionMarketing> findByCampanaId(Long campanaId);

    List<NotificacionMarketing> findByClienteId(Long clienteId);

    @Query("SELECT n FROM NotificacionMarketing n WHERE n.estado = 'PENDIENTE' AND n.fechaProgramada <= :ahora")
    List<NotificacionMarketing> findNotificacionesPendientesPorEnviar(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT n FROM NotificacionMarketing n WHERE n.campana.id = :campanaId AND n.estado = :estado")
    List<NotificacionMarketing> findByCampanaIdAndEstado(
            @Param("campanaId") Long campanaId,
            @Param("estado") String estado
    );

    @Query("SELECT COUNT(n) FROM NotificacionMarketing n WHERE n.campana.id = :campanaId AND n.estado = 'ENVIADO'")
    Long countEnviadosByCampanaId(@Param("campanaId") Long campanaId);

    @Query("SELECT COUNT(n) FROM NotificacionMarketing n WHERE n.campana.id = :campanaId AND n.estado = 'ABIERTO'")
    Long countAbiertosByCampanaId(@Param("campanaId") Long campanaId);

    @Query("SELECT COUNT(n) FROM NotificacionMarketing n WHERE n.campana.id = :campanaId AND n.estado = 'CLIC'")
    Long countClicsByCampanaId(@Param("campanaId") Long campanaId);

    @Query("SELECT n FROM NotificacionMarketing n WHERE n.estado = 'FALLIDO' AND n.intentosEnvio < 3")
    List<NotificacionMarketing> findNotificacionesParaReintentar();

    @Query("SELECT n.tipo, COUNT(n) FROM NotificacionMarketing n WHERE n.fechaEnvio >= :fechaInicio GROUP BY n.tipo")
    List<Object[]> getEstadisticasPorTipo(@Param("fechaInicio") LocalDateTime fechaInicio);
}
