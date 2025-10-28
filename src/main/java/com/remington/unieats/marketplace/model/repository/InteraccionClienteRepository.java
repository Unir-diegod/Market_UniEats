package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.InteraccionCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InteraccionClienteRepository extends JpaRepository<InteraccionCliente, Long> {

    List<InteraccionCliente> findByClienteId(Long clienteId);

    List<InteraccionCliente> findByTipo(String tipo);

    List<InteraccionCliente> findByCanal(String canal);

    @Query("SELECT i FROM InteraccionCliente i WHERE i.cliente.id = :clienteId ORDER BY i.fechaInteraccion DESC")
    List<InteraccionCliente> findByClienteIdOrderByFechaDesc(@Param("clienteId") Long clienteId);

    @Query("SELECT i FROM InteraccionCliente i WHERE i.fechaInteraccion >= :fechaInicio AND i.fechaInteraccion <= :fechaFin")
    List<InteraccionCliente> findInteraccionesPorPeriodo(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT i FROM InteraccionCliente i WHERE i.campana.id = :campanaId")
    List<InteraccionCliente> findByCampanaId(@Param("campanaId") Long campanaId);

    @Query("SELECT COUNT(i) FROM InteraccionCliente i WHERE i.cliente.id = :clienteId AND i.tipo = :tipo")
    Long countInteraccionesByClienteIdAndTipo(@Param("clienteId") Long clienteId, @Param("tipo") String tipo);

    @Query("SELECT i.tipo, COUNT(i) FROM InteraccionCliente i WHERE i.fechaInteraccion >= :fechaInicio GROUP BY i.tipo")
    List<Object[]> getInteraccionesPorTipo(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT SUM(i.valorMonetario) FROM InteraccionCliente i WHERE i.cliente.id = :clienteId AND i.tipo = 'COMPRA'")
    Double getTotalComprasByCliente(@Param("clienteId") Long clienteId);
}
