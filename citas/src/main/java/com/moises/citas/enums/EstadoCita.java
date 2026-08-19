package com.moises.citas.enums;


import com.moises.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum EstadoCita {
    PENDIENTE(1L, "Pendiente de confirmar", true, true) {
        @Override
        public Set<EstadoCita> puedeCambiar() {
            return EnumSet.of(CONFIRMADA, CANCELADA);
        }
    },
    CONFIRMADA(2L, "Confirmada por el paciente", true, false) {
        @Override
        public Set<EstadoCita> puedeCambiar() {
            return EnumSet.of(EN_CURSO, CANCELADA);
        }
    },
    EN_CURSO(3L, "Paciente llegó a su cita", true, false) {
        @Override
        public Set<EstadoCita> puedeCambiar() {
            return EnumSet.of(FINALIZADA);
        }
    },
    FINALIZADA(4L, "Cita finalizada", false, true) {
        @Override
        public Set<EstadoCita> puedeCambiar() {
            return Set.of();
        }
    },
    CANCELADA(5L,"Cita cancelada", false, true ) {
        @Override
        public Set<EstadoCita> puedeCambiar() {
            return Set.of();
        }
    };

    private final Long codigo;
    private final String descripcion;

    private final boolean actualizable;
    private final boolean eliminable;

    public abstract Set<EstadoCita> puedeCambiar();

    public boolean puedeCambiarA(EstadoCita nuevoEstado) {
        return puedeCambiar().contains(nuevoEstado);
    }

    public static EstadoCita obtenerEstadoCitaPorCodigo(Long codigo) {
        for (EstadoCita e : values()) {
            if (Objects.equals(e.codigo, codigo)) {
                return e;
            }
        }
        throw new RecursoNoEncontradoException("Código de cita no válido: " + codigo);
    }
}
