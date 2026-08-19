package com.moises.medicos.service;

import com.moises.commons.dto.medicos.MedicoRequest;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.service.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
    void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);
}
