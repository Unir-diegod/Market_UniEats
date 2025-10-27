package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuario(Usuario usuario);

    Optional<Cliente> findByUsuarioId(Integer usuarioId);

    List<Cliente> findByAceptaMarketingTrue();

    List<Cliente> findByAceptaEmailTrue();

    List<Cliente> findByNivelCliente(String nivelCliente);

    @Query("SELECT c FROM Cliente c WHERE c.valorTotalCompras >= :minValue ORDER BY c.valorTotalCompras DESC")
    List<Cliente> findTopClientesByValorCompras(@Param("minValue") Double minValue);

    @Query("SELECT c FROM Cliente c WHERE c.ultimaCompra >= :fechaInicio")
    List<Cliente> findClientesActivosDesde(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT c FROM Cliente c WHERE c.ultimaCompra < :fechaLimite OR c.ultimaCompra IS NULL")
    List<Cliente> findClientesInactivos(@Param("fechaLimite") LocalDateTime fechaLimite);

    @Query("SELECT c FROM Cliente c WHERE c.numeroPedidos >= :minPedidos ORDER BY c.numeroPedidos DESC")
    List<Cliente> findClientesFrecuentes(@Param("minPedidos") Integer minPedidos);

    @Query("SELECT c FROM Cliente c WHERE c.categoriaFavorita = :categoria")
    List<Cliente> findByCategoriaFavorita(@Param("categoria") String categoria);

    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.fechaRegistro >= :fechaInicio")
    Long countClientesNuevos(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT AVG(c.ticketPromedio) FROM Cliente c")
    Double getTicketPromedioGeneral();

    @Query("SELECT c FROM Cliente c WHERE c.puntosFidelidad >= :minPuntos ORDER BY c.puntosFidelidad DESC")
    List<Cliente> findTopClientesByPuntosFidelidad(@Param("minPuntos") Integer minPuntos);
}
