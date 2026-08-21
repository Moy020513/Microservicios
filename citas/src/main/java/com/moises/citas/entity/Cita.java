package com.moises.citas.entity;


import com.moises.citas.enums.EstadoCita;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.utils.StringCustomUtils;
import com.moises.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CITAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long id;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_MEDICO", nullable = false)
    private Long idMedico;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "SINTOMAS", nullable = false, length = 500)
    private String sintomas;

    @Column(name = "ESTADO_CITA", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;

    @Column(name = "ESTADO_REGISTRO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;

    private static void validarId(Long id, String campo) {

        ValoresNumericosUtils.validarLongPositivo(id, "El id del " + campo +
                "es requerido y debe ser positivo");
    }

    private static void validarFecha(LocalDateTime fechaCita) {
        if (fechaCita == null || !fechaCita.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("La fecha de la cita es requerida y debe ser futura");
    }

    public static void validarDatos(
            Long idPaciente, Long idMedico,
            LocalDateTime fechaCita, String sintomas) {

        validarId(idPaciente, "paciente");

        validarId(idMedico, "médico");

        validarFecha(fechaCita);

        StringCustomUtils.validarTamanio(sintomas, 20, 500,
                "Los síntomas son requeridos y deben ser entre 20 y 500 caracteres");
    }

    private void validarNoEliminada() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("La cita ya está eliminada");
    }

    private void validarEliminacionPermitida() {
        validarNoEliminada();
        if (!estadoCita.isEliminable())
            throw new IllegalStateException(
                    "La cita con estado " + estadoCita
                    + "no puede eliminarse"
            );
    }

    private void validarActualizacionPermitida() {
        validarNoEliminada();
        if (!estadoCita.isActualizable())
            throw new IllegalStateException(
                    "La cita con estado " + estadoCita
                            + "no puede actualizarse"
            );
    }


    public void eliminar() {
        validarEliminacionPermitida();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizar(
            Long idPaciente, Long idMedico,
            LocalDateTime fechaCita, String sintomas){
        validarActualizacionPermitida();
        validarDatos(idPaciente, idMedico, fechaCita, sintomas);
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaCita = fechaCita;
        this.sintomas = sintomas.trim();
    }

    public void actualizarEstadoCita(EstadoCita nnuevoEstado) {
        validarActualizacionPermitida();
        if (nnuevoEstado == null)
            throw new IllegalArgumentException("El nuevo estado de la cita es requerido");

        if (!estadoCita.puedeCambiarA(nnuevoEstado))
            throw new IllegalStateException("La cita con estado "
            + estadoCita + "solo puede cambiar a: "
            + estadoCita.puedeCambiar());
        this.estadoCita = nnuevoEstado;
    }

    public static Cita crear(
            Long idPaciente, Long idMedico,
            LocalDateTime fechaCita, String sintomas) {
        validarDatos(idPaciente, idMedico, fechaCita, sintomas);

        return Cita.builder()
                .idPaciente(idPaciente)
                .idMedico(idMedico)
                .fechaCita(fechaCita)
                .sintomas(sintomas.trim())
                .estadoCita(EstadoCita.PENDIENTE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }
}
