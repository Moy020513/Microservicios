package com.moises.citas.service;

import com.moises.citas.dto.CitaRequest;
import com.moises.citas.dto.CitaResponse;
import com.moises.citas.entity.Cita;
import com.moises.citas.enums.EstadoCita;
import com.moises.citas.mapper.CitaMapper;
import com.moises.citas.repository.CitaRepository;
import com.moises.commons.client.MedicoClient;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando todas las citas activas");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map( cita -> citaMapper.entidadAResponse(
                        cita,
                        null,
                        obtenerMedicoSinEstado(cita.getIdMedico())
                )).toList();
    }

    @Override
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOException(id);
        return citaMapper.entidadAResponse(
                cita,
                null,
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita...");

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        Cita cita = citaMapper.requestAEntidad(request);
        citaRepository.save(cita);

        log.info("Cita registrada exitósamente");

        return citaMapper.entidadAResponse(
                cita,
                null,
                medico
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = citaMapper.requestAEntidad(request);
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );

        log.info("Cita actualizada con id: {}", id);
        return citaMapper.entidadAResponse(
                cita,
                null,
                medico
        );
    }

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOException(idCita);

        log.info("Actualizando estado de la cita con id: {}", idCita);
        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));
        log.info("Estado de la cita {} actualizado correctamente", idCita);

    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOException(id);

        log.info("Actualizando estado de la cita con id: {}", id);
        cita.eliminar();
        log.info("Cita con id {} ha sido marcada como eliminada", id);

    }

    private Cita obtenerCitaOException(Long id) {
        log.info("Buscando cita con id: {}", id);

        return citaRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }

    private MedicoResponse obtenerMedicoActivo(Long id) {
        log.info("Buscando médico activo con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoPorIdSinEstado(id);
    }
}
