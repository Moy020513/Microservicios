package com.moises.citas.service;

import com.moises.citas.dto.CitaRequest;
import com.moises.citas.dto.CitaResponse;
import com.moises.commons.service.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {
    void actualizarEstadoCita(Long idCita, Long idEstadoCita);
}
