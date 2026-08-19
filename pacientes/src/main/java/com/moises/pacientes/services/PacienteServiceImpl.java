package com.moises.pacientes.services;


import com.moises.commons.dto.pacientes.PacienteRequest;
import com.moises.commons.dto.pacientes.PacienteResponse;
import com.moises.pacientes.entities.Pacientes;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.exceptions.RecursoNoEncontradoException;
import com.moises.pacientes.mappers.PacienteMapper;
import com.moises.pacientes.repositories.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PacienteServiceImpl implements PacienteService{

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        log.info("Listando todos los pacientes");

        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        return pacienteMapper.entidadAResponse(obtenerPacienteActivo(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientePorId(Long id) {

        return pacienteMapper.entidadAResponse(
                pacienteRepository.findById(id)
                        .orElseThrow(()->
                                new RecursoNoEncontradoException("Paciente no encontrado con id: " + id)));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        log.info("Registrando nuevo paciente");

        validarDatosUnicos(request);
        Pacientes pacientes = pacienteMapper.requestAEntidad(
                request
        );
        pacientes.calcularIMC();
        pacientes.generarNumeroExpediente();

        pacienteRepository.save(pacientes);
        log.info("Paciente registrado correctamente");
        return pacienteMapper.entidadAResponse(pacientes);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {
        Pacientes pacientes = obtenerPacienteActivo(id);
        log.info("Actualizando paciente con id: {}", id);

        validarCambiosUnicos(request, id);

        pacientes.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono(),
                request.direccion()
        );
        return pacienteMapper.entidadAResponse(pacientes);
    }

    @Override
    public void eliminar(Long id) {
        Pacientes pacientes = obtenerPacienteActivo(id);
        log.info("Eliminando paciente con id: {}", id);

        pacientes.setEstatusEliminado();
        log.info("Paciente con id {} eliminado correctamente", pacientes.getId());
    }

    private Pacientes obtenerPacienteActivo(Long id) {
        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() ->
        new RecursoNoEncontradoException("Paciente activo no encontrado con id: " + id));
    }


    private void validarDatosUnicos(PacienteRequest request) {
        log.info("Validando email único...");
        if (pacienteRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el email: " + request.email());
        }

        log.info("Validando teléfono único...");
        if (pacienteRepository.existsByTelefono(request.telefono().trim())) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el teléfono: " + request.telefono());
        }
    }

    private void validarCambiosUnicos(PacienteRequest request, Long id) {
        log.info("Validando email único para actualización...");
        if (pacienteRepository.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id)) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el email: " + request.email());
        }

        log.info("Validando teléfono único para actualización...");
        if (pacienteRepository.existsByTelefonoAndIdNot(request.telefono().trim(), id)) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el teléfono: " + request.telefono());
        }
    }
}
