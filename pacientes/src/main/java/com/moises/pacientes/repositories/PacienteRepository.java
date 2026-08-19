package com.moises.pacientes.repositories;

import com.moises.pacientes.entities.Pacientes;
import com.moises.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Pacientes, Long> {

    Optional<Pacientes> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    List<Pacientes> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByTelefono(String telefono);

    boolean existsByTelefonoAndIdNot(String telefono, Long id);

    boolean existsByNumExpediente(String numExpediente);

}
