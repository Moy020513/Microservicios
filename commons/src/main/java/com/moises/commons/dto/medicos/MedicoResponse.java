package com.moises.commons.dto.medicos;

public record MedicoResponse(
        Long id,
        String nombre,
        Short edad,
        String email,
        String telefono,
        String cedulaProfesional,
        String especialidad,
        String disponibilidad,
        Long idDisponibilidad
) {
}
