package com.moises.citas.repository;

import com.moises.citas.entity.Cita;
import com.moises.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
}
