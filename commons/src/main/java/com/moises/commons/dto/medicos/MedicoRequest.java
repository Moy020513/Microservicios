package com.moises.commons.dto.medicos;

import jakarta.validation.constraints.*;

public record MedicoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El apellido paterno debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @NotNull(message = "La edad es requerida")
        @Min(value = 18, message = "La edad mínima es de 18 años")
        @Max(value = 100, message = "La edad debe ser menor o igual a 100")
        Short edad,

        @Email(message = "El email debe tener formato de correo (correo@dominio)")
        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
        String telefono,

        @NotBlank(message = "La cédula profesional es requerida")
        @Size(min = 12, max = 12, message = "La cédula profesional debe tener exactamente 12 caracteres")
        String cedulaProfesional,

        @NotNull(message = "El id de la especialidad es requerido")
        @Positive(message = "El id de la especiadlidad debe ser positivo")
        Long idEspecialidad

        ) {
}
