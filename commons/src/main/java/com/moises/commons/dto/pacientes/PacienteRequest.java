package com.moises.commons.dto.pacientes;

import jakarta.validation.constraints.*;

public record PacienteRequest(
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
        @Min(value = 1, message = "La edad debe ser mayor o igual a 1")
        @Max(value = 100, message = "La edad debe ser menor o igual a 100")
        Short edad,

        @NotNull(message = "El peso es requerido")
        @DecimalMin(value = "0.1", message = "El peso debe ser mayor o igual a 0.1 kg")
        @DecimalMax(value = "200.0", message = "El peso debe ser menor o igual a 200 kg")
        Double peso,

        @NotNull(message = "La estatura es requerida")
        @DecimalMin(value = "1.0", message = "La estatura debe ser mayor o igual a 1.0 m")
        @DecimalMax(value = "2.0", message = "La estatura debe ser menor o igual a 2.0 m")
        Double estatura,

        @Email(message = "El email debe tener formato de correo")
        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 8 y 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
                String telefono,

        @NotBlank(message = "La dirección es requerida")
        @Size(min = 1, max = 150, message = "La dirección debe tener entre 1 y 150 caracteres")
        String direccion

        ) {
}
