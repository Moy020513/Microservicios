package com.moises.commons.enums;


import com.moises.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor @Getter
public enum EspecialidadMedico {
    MEDICINA_GENERAL(1L, "Medicina General"),
    PEDIATRIA(2L, "Pediatría"),
    CARDIOLOGIA(3L, "Cardiología"),
    DERMATOLOGIA(4L, "Dermatología"),
    NEUROLOGIA(5L, "Neurología"),
    GINECOLOGIA(6L, "Ginecología y Obstetricia"),
    PSIQUIATRIA(7L, "Psiquiatría"),
    TRAUMATOLOGIA(8L, "Traumatología y Ortopedia"),
    ONCOLOGIA(9L, "Oncología"),
    OTORRINOLARINGOLOGIA(10L, "Otorrinolaringología"),
    OFTALMOLOGIA(11L, "Oftalmología"),
    ENDOCRINOLOGIA(12L, "Endocrinología"),
    NEFROLOGIA(13L, "Nefrología"),
    REUMATOLOGIA(14L, "Reumatología"),
    UROLOGIA(15L, "Urología");

    private final Long codigo;

    private final String desripcion;

    public static EspecialidadMedico obtenerEspecialidadPorCodigo(Long codigo) {
        for (EspecialidadMedico e : values()) {
            if (Objects.equals(e.codigo, codigo))
                return e;
        }
        throw new RecursoNoEncontradoException("Código de especialidad no válido:" + codigo);
    }
}
