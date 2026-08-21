package com.moises.commons.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas")
public interface CitaClient {

    @GetMapping("/paciente/{idPaciente}/tiene-citas-activas")
    Boolean CitasActivasPaciente(@PathVariable Long idPaciente);

    @GetMapping("/medico/{idMedico}/tiene-citas-activas")
    Boolean CitasActivasMedico(@PathVariable Long idMedico);
}
