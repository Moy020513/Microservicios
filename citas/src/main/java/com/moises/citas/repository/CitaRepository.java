package com.moises.citas.repository;

import com.moises.citas.entity.Cita;
import com.moises.citas.enums.EstadoCita;
import com.moises.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
            Long idPaciente,
            EstadoRegistro estadoRegistro,
            Collection<EstadoCita> estados);

    boolean existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
            Long idMedico,
            EstadoRegistro estadoRegistro,
            Collection<EstadoCita> estados);

    boolean existsByIdPacienteAndIdNotAndEstadoRegistroAndEstadoCitaIn(
            Long idPaciente,
            Long idCita,
            EstadoRegistro estadoRegistro,
            Collection<EstadoCita> estados);

    boolean existsByIdMedicoAndIdNotAndEstadoRegistroAndEstadoCitaIn(
            Long idMedico,
            Long idCita,
            EstadoRegistro estadoRegistro,
            Collection<EstadoCita> estados);

    boolean existsByIdMedicoAndEstadoRegistroAndEstadoCita(
            Long idMedico,
            EstadoRegistro estadoRegistro,
            EstadoCita estadoCita
    );

    boolean existsByIdMedicoAndIdNotAndEstadoRegistroAndEstadoCita(
            Long idMedico,
            Long idCita,
            EstadoRegistro estadoRegistro,
            EstadoCita estadoCita
    );
}
