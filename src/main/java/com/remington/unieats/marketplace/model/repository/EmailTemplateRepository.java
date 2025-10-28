package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    List<EmailTemplate> findByActivoTrue();

    Optional<EmailTemplate> findByNombre(String nombre);

    List<EmailTemplate> findByCategoria(String categoria);

    @Query("SELECT e FROM EmailTemplate e WHERE e.activo = true ORDER BY e.fechaCreacion DESC")
    List<EmailTemplate> findTemplatesActivosRecientes();

    @Query("SELECT COUNT(e) FROM EmailTemplate e WHERE e.activo = true")
    Long countTemplatesActivos();
}
