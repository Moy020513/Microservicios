package com.moises.commons.enums;


import com.moises.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum DisponibilidadMedico {

    DISPONIBLE(1L, "Disponible para atender pacientes"),
    EN_CONSULTA(2L, "Atendiendo un paciente actualmente"),
    FUERA_DE_TURNO(3L, "No se encuentra en turno"),
    DE_GUARDIA(4L, "Disponible bajo guardia"),
    NO_DISPONIBLE(5L, "No disponible por el momento");

    private final Long codigo;

    private final String desripcion;

    public static DisponibilidadMedico obtenerDisponibilidadPorCodigo(Long codigo) {
        for (DisponibilidadMedico d : values()) {
            if (Objects.equals(d.codigo, codigo))
                return d;
        }
        throw new RecursoNoEncontradoException("Código de disponibilidad no válido:" + codigo);
    }
}
