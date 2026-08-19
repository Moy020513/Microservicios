package com.moises.citas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "El id del paciente es requerido")
        @Positive(message = "El id del paciente debe ser positivo")
        Long idPaciente,

        @NotNull(message = "El id del médico es requerido")
        @Positive(message = "El id del médico debe ser positivo")
        Long idMedico,

        @NotNull(message = "La fecha de la cita es requerida")
        @Positive(message = "La fecha de la cita debe ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/YYYY HH:mm")
        LocalDateTime fechaCita,

        @NotBlank(message = "Los síntomas son requeridos")
        @Size(min = 20, max = 500,
            message = "La descripción de los síntomas debe tener entre 20 y 500 caracteres")
        @Positive(message = "El id del paciente debe ser positivo")
        String sintomas
) {
}
