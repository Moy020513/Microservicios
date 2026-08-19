package com.moises.pacientes.services;

import com.moises.commons.dto.pacientes.PacienteRequest;
import com.moises.commons.dto.pacientes.PacienteResponse;

import com.moises.commons.service.CrudService;
import java.util.List;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {
    PacienteResponse obtenerPacientePorId(Long id);
    void eliminar(Long id);



}
