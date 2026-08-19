package com.moises.medicos.service;


import com.moises.commons.dto.medicos.MedicoRequest;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.enums.DisponibilidadMedico;
import com.moises.commons.enums.EspecialidadMedico;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.exceptions.RecursoNoEncontradoException;
import com.moises.medicos.entity.Medico;
import com.moises.medicos.mapper.MedicoMapper;
import com.moises.medicos.repository.MedicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Override
    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
        Medico medico = obtenerMedicoActivoOException(idMedico);
        log.info("Actualizando disponibilidad del médico con id: {}", idMedico);

        DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico
                .obtenerDisponibilidadPorCodigo(idDisponibilidad);

        DisponibilidadMedico disponibilidadAnterior = medico.getDisponibilidad();

        medico.actualizarDisponibilidad(nuevaDisponibilidad);
        log.info("Disponibilidad del médico con id {} cambió de {} a {}", idMedico
        , disponibilidadAnterior, nuevaDisponibilidad);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        log.info("Listando todos los médicos activos");

        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(medicoMapper::entidadAResponse).toList();
    }

    @Override
    public MedicoResponse obtenerPorId(Long id) {
        return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
    }

    @Override
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
        log.info("Buscando médico sin estado con id: {}", id);
        return medicoMapper.entidadAResponse(medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico sin estado no encontrado con id: " + id)
                ));
    }

    @Override
    public MedicoResponse registrar(MedicoRequest request) {
        log.info("Registrando nuevo médico sin estado con id: {}", request.nombre());
        validarDatosUnicos(request);
        Medico medico = medicoMapper.requestAEntidad(request);
        medico.actualizarEspecialidad(
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad())
        );
        medicoRepository.save(medico);

        log.info("Nuevo médico registrado: {}", medico.getNombre());

        return  medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoActivoOException(id);
        log.info("Actualizando médico con id: {}", id);
        validarCambiosUnicos(request, id);

        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad())
        );

        log.info("Médico actualizado exitósamente");
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoActivoOException(id);
        log.info("Eliminando médico con id: {}", id);
        medico.eliminar();

    }

    private Medico obtenerMedicoActivoOException(long id) {
        log.info("Buscando médico activo con id: {}", id);
        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico activo no encontrado con id: " + id
        ));
    }

    private void validarDatosUnicos(MedicoRequest request) {
        log.info("Validando email único");

        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(
                request.email().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalStateException("Ya existe un médico activo registrado con el email: " + request.email());
        log.info("Validando teléfono único...");

        if (medicoRepository.existsByTelefonoAndEstadoRegistro(
                request.telefono().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el teléfono:" + request.telefono());

        log.info("Validando cédula profesional única...");

        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(
                request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con la cédula profesional: "
            + request.cedulaProfesional());
    }

    private void validarCambiosUnicos(MedicoRequest request, Long id) {
        log.info("Validando cambio en email único");

        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id))
            throw new IllegalStateException("Ya existe un médico activo registrado con el email: " + request.email());
        log.info("Validando cambio en teléfono único...");

        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con el teléfono:" + request.telefono());

        log.info("Validadndo cambio en cédula profesional única...");

        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(
                request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico activo registrado con la cédula profesional: "
                    + request.cedulaProfesional());
    }
}
