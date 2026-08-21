package com.moises.citas.service;

import com.moises.citas.dto.CitaRequest;
import com.moises.citas.dto.CitaResponse;
import com.moises.citas.entity.Cita;
import com.moises.citas.enums.EstadoCita;
import com.moises.citas.mapper.CitaMapper;
import com.moises.citas.repository.CitaRepository;
import com.moises.commons.client.MedicoClient;
import com.moises.commons.client.PacienteClient;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.dto.pacientes.PacienteResponse;
import com.moises.commons.enums.DisponibilidadMedico;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteCliente;

    private  final List<EstadoCita> ESTADOS_CITA_CONFIRMADA_O_EN_CURSO =
            List.of(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);

    private  final List<EstadoCita> ESTADOS_CITA_ACTIVAS =
            List.of(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);

    private  final List<EstadoCita> ESTADOS_CITA_PENDIENTE_O_CONFIRMADA =
            List.of(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA);

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando todas las citas activas");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map( cita -> citaMapper.entidadAResponse(
                        cita,
                        obtenerPacienteSinEstado(cita.getIdPaciente()),
                        obtenerMedicoSinEstado(cita.getIdMedico())
                )).toList();
    }

    @Override
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOException(id);
        return citaMapper.entidadAResponse(
                cita,
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita...");

        PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());
        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        validarPacienteSinOtraCita(request.idPaciente());
        validarMedicoDisponible(medico);
        validarMedicoSinOtraCita(request.idMedico());

        Cita cita = citaMapper.requestAEntidad(request);
        citaRepository.save(cita);

        medicoClient.actualizarDisponibilidadMedico(
                cita.getIdMedico(),
                DisponibilidadMedico.NO_DISPONIBLE.getCodigo()

        );


        log.info("Cita registrada exitósamente");

        return citaMapper.entidadAResponse(
                cita,
                paciente,
                medico
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOException(id);

        if(EstadoCita.EN_CURSO.equals(cita.getEstadoCita())){
            throw new IllegalStateException("La cita no permite modificaciones en su estado actual");
        }

        Long idPacienteAnterior = cita.getIdPaciente();
        Long idMedicoAnterior = cita.getIdMedico();

        boolean cambioPaciente = !Objects.equals(idPacienteAnterior, request.idPaciente());
        boolean cambioMedico = !Objects.equals(idMedicoAnterior, request.idMedico());

        PacienteResponse paciente;
        MedicoResponse medico;

        if(cambioPaciente){
            paciente = obtenerPacienteActivo(request.idPaciente());
            validarPacienteSinOtraCita(request.idPaciente(), id);
        }else{
            paciente = obtenerPacienteActivo(idPacienteAnterior);
        }

        if(cambioMedico){
            medico = obtenerMedicoActivo(request.idMedico());
            validarMedicoDisponible(medico);
            validarPacienteSinOtraCita(request.idMedico(), id);
        }else{
            medico = obtenerMedicoActivo(idMedicoAnterior);
        }

        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );

        // Si cambió el médico, sincronizar disponibilidades
        if (cambioMedico) {
            // Libera al médico anterior
            sincronizarDisponibilidadMedico(idMedicoAnterior, cita.getId());
            // Ocupa al nuevo médico según el estado de la cita
            medicoClient.actualizarDisponibilidadMedico(
                    cita.getIdMedico(),
                    DisponibilidadMedico.NO_DISPONIBLE.getCodigo()
            );
        }

        log.info("Cita actualizada con id: {}", id);
        return citaMapper.entidadAResponse(
                cita,
                paciente,
                medico
        );
    }

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOException(idCita);
//revisar, else está de más, utilizar la función del enum y a qué estados puede cambiar
        log.info("Actualizando estado de la cita con id: {}", idCita);
        EstadoCita nuevoEstado = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);
        cita.actualizarEstadoCita(nuevoEstado);

        if(nuevoEstado == EstadoCita.FINALIZADA || nuevoEstado == EstadoCita.CANCELADA){
            sincronizarDisponibilidadMedico(cita.getIdMedico(), cita.getId());
        }else{
            medicoClient.actualizarDisponibilidadMedico(cita.getIdMedico(),
                    obtenerNuevaDisponibilidadMedico(idCita));
        }

        log.info("Estado de la cita {} actualizado correctamente", idCita);

    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOException(id);
//revisar este eliminar
        log.info("Eliminando cita con id: {}", id);
        cita.eliminar();
        sincronizarDisponibilidadMedico(cita.getIdMedico(), cita.getId());
        log.info("Cita con id {} ha sido marcada como eliminada", id);

    }

    @Override
    @Transactional(readOnly = true)
    public Boolean CitasActivasPaciente(Long idPaciente) {
//listas a una constante arriba,
        return citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
                idPaciente,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_CONFIRMADA_O_EN_CURSO
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean CitasActivasMedico(Long idMedico) {

        return citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
                idMedico,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_CONFIRMADA_O_EN_CURSO
        );
    }

    private Cita obtenerCitaOException(Long id) {
        log.info("Buscando cita con id: {}", id);

        return citaRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }

    private void validarMedicoDisponible(MedicoResponse medico){

        if(!Objects.equals(
                medico.idDisponibilidad(),
                DisponibilidadMedico.DISPONIBLE.getCodigo())){
            throw new IllegalStateException(
                    "El médico no se encuentra disponible para recibir una nueva cita."
            );
        }
    }

    private void validarPacienteSinOtraCita(Long idPaciente){

        if(citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
                idPaciente,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_ACTIVAS))
        {

            throw new IllegalStateException(
                    "El paciente ya tiene una cita pendiente, confirmada o en curso."
            );
        }
    }

    private void validarMedicoSinOtraCita(Long idMedico){

        if(citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
                idMedico,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_ACTIVAS)){

            throw new IllegalStateException(
                    "El médico ya tiene una cita pendiente, confirmada o en curso."
            );
        }
    }

    private void validarPacienteSinOtraCita(Long idPaciente, Long idCita){

        if(citaRepository.existsByIdPacienteAndIdNotAndEstadoRegistroAndEstadoCitaIn(
                idPaciente,
                idCita,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_ACTIVAS)){

            throw new IllegalStateException(
                    "El paciente ya tiene otra cita pendiente, confirmada o en curso."
            );
        }
    }

    private MedicoResponse obtenerMedicoActivo(Long id) {
        log.info("Buscando médico activo con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    private MedicoResponse obtenerMedicoSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoPorIdSinEstado(id);
    }

    private void sincronizarDisponibilidadMedico(Long idMedico, Long idCitaExcluir){

        if(citaRepository.existsByIdMedicoAndIdNotAndEstadoRegistroAndEstadoCitaIn(
                idMedico,
                idCitaExcluir,
                EstadoRegistro.ACTIVO,
                ESTADOS_CITA_PENDIENTE_O_CONFIRMADA
        )){
            medicoClient.actualizarDisponibilidadMedico(
                    idMedico, DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
            return;
        }

        medicoClient.actualizarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());
    }

    private PacienteResponse obtenerPacienteActivo(Long id){
        log.info("Buscando paciente activo con id: {} en el servicio remoto...", id);
        return pacienteCliente.obtenerPacienteActivoPorId(id);
    }
    private PacienteResponse obtenerPacienteSinEstado(Long id){
        log.info("Buscando paciente sin estado con id: {} en el servicio remoto...", id);
        return pacienteCliente.obtenerPacienteSinEstadoPorId(id);
    }

    private Long obtenerNuevaDisponibilidadMedico(Long idEstadoCita) {
        EstadoCita estadoCita = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);
        return switch (estadoCita) {
            case PENDIENTE, CONFIRMADA -> DisponibilidadMedico.NO_DISPONIBLE.getCodigo();
            case EN_CURSO -> DisponibilidadMedico.EN_CONSULTA.getCodigo();
            case FINALIZADA, CANCELADA -> DisponibilidadMedico.DISPONIBLE.getCodigo();
        };
    }
}
