package com.moises.medicos.mapper;

import com.moises.commons.dto.medicos.MedicoRequest;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.enums.DisponibilidadMedico;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.mapper.CommonMapper;
import com.moises.medicos.entity.Medico;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico> {

    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        if (request == null) return null;

        return Medico.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .email(request.email().toLowerCase().trim())
                .telefono(request.telefono().trim())
                .cedulaProfesional(request.cedulaProfesional().trim())
                .disponibilidad(DisponibilidadMedico.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();

    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        if(entidad == null) return null;
        return new MedicoResponse(
          entidad.getId(),
          String.join(" ",
                    entidad.getNombre(),
                    entidad.getApellidoPaterno(),
                    entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedulaProfesional(),
                entidad.getEspecialidad().getDesripcion(),
                entidad.getDisponibilidad().getDesripcion(),
                entidad.getDisponibilidad().getCodigo()
        );
    }
}
