package com.moises.pacientes.mappers;

import com.moises.commons.dto.pacientes.PacienteRequest;
import com.moises.commons.dto.pacientes.PacienteResponse;

import com.moises.pacientes.entities.Pacientes;
import com.moises.commons.enums.EstadoRegistro;
import org.springframework.stereotype.Component;


@Component
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteResponse, Pacientes>{

    @Override
    public Pacientes requestAEntidad(PacienteRequest request) {
        if(request == null) return null;

        return Pacientes.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .peso(request.peso())
                .estatura(request.estatura())
                .email(request.email().trim())
                .telefono(request.telefono().trim())
                .direccion(request.direccion().trim())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public PacienteResponse entidadAResponse(Pacientes entidad) {
            if(entidad == null) return null;

        return new PacienteResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getPeso(),
                entidad.getEstatura(),
                entidad.getImc(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getNumExpediente()
        );
    }
}
