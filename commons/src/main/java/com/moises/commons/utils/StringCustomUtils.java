package com.moises.commons.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringCustomUtils {

    public static final DateTimeFormatter FORMATOFECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATOHORA = DateTimeFormatter.ofPattern("HH:mm");
    public static void validarNoVacio(String texto, String mensaje) {
        if (texto == null || texto.isBlank())
            throw new IllegalArgumentException(mensaje);
    }

    public static void validarTamanio(String texto, Integer min, Integer max, String mensaje) {
        validarNoVacio(texto, mensaje);

        if (texto.length() < min || texto.length() > max)
            throw new IllegalArgumentException(mensaje);
    }

    public static String quitarAcentos(String texto) {
        return texto.toLowerCase()
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace("ü", "u");
    }

    public static String localDateAAstring(LocalDate fecha) {
        return fecha == null ? null : fecha.format(FORMATOFECHA);
    }
}