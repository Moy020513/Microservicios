package com.moises.medicos.controller;


import com.moises.commons.controller.CommonController;
import com.moises.commons.dto.medicos.MedicoRequest;
import com.moises.commons.dto.medicos.MedicoResponse;
import com.moises.commons.mapper.CommonMapper;
import com.moises.medicos.service.MedicoService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService> {
    public MedicoController(MedicoService service) {
        super(service);
    }

    @GetMapping("/id-medico/{id}")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
    }

    @PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
    public ResponseEntity<Void> actualizarDisponibiliadMedico(
            @PathVariable @Positive(message = "El idMedico debe ser positivo") Long idMedico,
            @PathVariable @Positive(message = "El idDesponibilidad debe ser positivo") Long idDisponibilidad
    ){
        service.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);
        return ResponseEntity.noContent().build();
    }
}
