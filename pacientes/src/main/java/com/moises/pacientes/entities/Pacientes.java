package com.moises.pacientes.entities;

import com.moises.commons.enums.EstadoRegistro;
import com.moises.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    private Short edad;

    @Column(name = "PESO", nullable = false )
    private Double peso;

    @Column(name = "ESTATURA", nullable = false)
    private Double estatura;

    @Column(name = "IMC", nullable = false)
    private Double imc;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "NUM_EXPEDIENTE", nullable = false, length = 20, unique = true)
    private String numExpediente;

    @Column(name = "TELEFONO", nullable = false, length = 10, unique = true)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    private String direccion;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;


    private void validarDatos(String nombre, String apellidoPaterno,
                              String apellidoMaterno, Short edad, Double peso,
                              Double estatura, String email, String telefono,
                              String direccion) {

        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y debe tener entre 1 y 100 caracteres");

        StringCustomUtils.validarTamanio(direccion, 1, 150,
                "La dirección es requerida y debe tener entre 1 y 150 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El teléfono es requerido y debe tener exactamente 10 dígitos numéricos");

        if (edad == null || edad < 1 || edad > 100) {
            throw new IllegalArgumentException("La edad debe estar entre 1 y 100 años");
        }

        if (peso == null || peso < 0.1 || peso > 200) {
            throw new IllegalArgumentException("El peso debe estar entre 0.1 y 200 kg");
        }

        if (estatura == null || estatura < 1.0 || estatura > 2.0) {
            throw new IllegalArgumentException("La estatura debe estar entre 1.0 y 2.0 metros");
        }
    }


    public void calcularIMC() {
        this.imc = peso / (estatura * estatura);
    }

    public void generarNumeroExpediente() {

        StringBuilder expediente = new StringBuilder();
        for (char digito : telefono.toCharArray()) {
            expediente.append(digito).append('X');
        }
        this.numExpediente = expediente.toString();
    }


    public void actualizar(String nombre, String apellidoPaterno,
                           String apellidoMaterno, Short edad, Double peso,
                           Double estatura, String email, String telefono, String direccion) {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, edad, peso, estatura,
                email, telefono, direccion);
        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.email = email.trim();
        this.telefono = telefono;
        this.direccion = direccion;

        calcularIMC();
        generarNumeroExpediente();
    }
    public void setEstatusEliminado() {
        estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
