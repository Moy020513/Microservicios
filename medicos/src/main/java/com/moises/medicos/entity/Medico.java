package com.moises.medicos.entity;


import com.moises.commons.enums.DisponibilidadMedico;
import com.moises.commons.enums.EspecialidadMedico;
import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.utils.StringCustomUtils;
import com.moises.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    private Short edad;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Column(name = "CEDULA_PROFESIONAL", nullable = false, length = 12)
    private String cedulaProfesional;


    @Enumerated(EnumType.STRING)
    @Column(name = "ESPECIALIDAD", nullable = false)
    private EspecialidadMedico especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDAD", nullable = false)
    private DisponibilidadMedico disponibilidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad,
                  String email, String telefono, String cedulaProfesional,
                  EspecialidadMedico especialidad) {

        StringCustomUtils.validarTamanio(nombre,  1,  50,
                "El nombre es requerido y debe contener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno,  1,  50,
                "El apellidoPaterno es requerido y debe contener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno,  1,  50,
                "El apellidoPaterno es requerido y debe contener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(email,  1,  100,
                "El email es requerido y debe contener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(telefono,  10,  10,
                "El teléfono es requerido y debe contener exactamente 10 dígitos(0-9)");

        StringCustomUtils.validarTamanio(cedulaProfesional,  12,  12,
                "La cedula profesional es requerida y debe contener exactamente 12 caracteres");

        ValoresNumericosUtils.validarRangoShort(edad, (short) 18, (short) 100,
                "La edad es requerida y debe tener entre 18 y 100 años");

        if (especialidad == null)
            throw new IllegalArgumentException("La especialidad es requerida");
    }
            private void validarNoEliminado() {
                if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
                    throw new IllegalStateException("El médico ya está eliminado");
            }

        public void actualizarEspecialidad(EspecialidadMedico especialidad) {
            validarNoEliminado();

            if (especialidad == null)
                throw new IllegalArgumentException("La especialidad es requerida");

            this.especialidad = especialidad;
        }

        public void actualizarDisponibilidad(DisponibilidadMedico disponibilidad) {
            validarNoEliminado();

            if (disponibilidad == null)
                throw new IllegalArgumentException("La disponibilidad es requerida");

            this.disponibilidad = disponibilidad;
        }

        public void eliminar() {

        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
        }

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno,
                           Short edad, String email, String telefono,
                           String codulaProfesional, EspecialidadMedico especialidad) {

        validarNoEliminado();

        validarDatos(
                nombre, apellidoPaterno, apellidoMaterno,
                edad, email, telefono, codulaProfesional, especialidad);

        actualizarEspecialidad(especialidad);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.cedulaProfesional = codulaProfesional.trim();
    }
    }

