package com.moises.pacientes.controllers;


import com.moises.commons.controller.CommonController;
import com.moises.commons.dto.pacientes.PacienteRequest;
import com.moises.commons.dto.pacientes.PacienteResponse;
import com.moises.pacientes.services.PacienteService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class PacientesController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {
    public PacientesController(PacienteService service) {
        super(service);
    }

    @GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteResponse> obtenerPacientePorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id){
        return ResponseEntity.ok(service.obtenerPacientePorId(id));
    }
}
