package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.Segmento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentoRepository extends JpaRepository<Segmento, Long> {

    List<Segmento> findByActivoTrue();

    Optional<Segmento> findByNombre(String nombre);

    @Query("SELECT s FROM Segmento s WHERE s.cantidadClientes >= :minClientes ORDER BY s.cantidadClientes DESC")
    List<Segmento> findSegmentosGrandes(@Param("minClientes") Integer minClientes);

    @Query("SELECT s FROM Segmento s JOIN s.clientes c WHERE c.id = :clienteId")
    List<Segmento> findSegmentosByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT COUNT(s) FROM Segmento s WHERE s.activo = true")
    Long countSegmentosActivos();
}
