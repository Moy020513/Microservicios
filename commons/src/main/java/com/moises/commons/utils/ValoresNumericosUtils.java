package com.moises.commons.utils;

import java.math.BigDecimal;

public class ValoresNumericosUtils {

    public static <N extends Number> void validarNumeroRequerido(N numero) {
        if (numero == null)
            throw new IllegalArgumentException("El valor numérico es requerido");
    }
    public static void validarLongPositivo(Long numero, String mensaje) {
        validarNumeroRequerido(numero);

        if (numero < 0)
            throw new IllegalArgumentException(mensaje);
    }

    public static void validarEnteroPositivo(Integer numero, String mensaje) {
        validarNumeroRequerido(numero);

        if (numero < 0)
            throw new IllegalArgumentException(mensaje);
    }

    public static void validarBigDecimalPositivo(BigDecimal numero, String mensaje) {
        validarNumeroRequerido(numero);

        if (numero.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException(mensaje);
    }

    public static void validarRangoShort(Short numero, short min, short max, String mensaje) {
        validarNumeroRequerido(numero);

        if (numero < min || numero > max)
            throw new IllegalArgumentException(mensaje);
    }

    public static void validarRangoDouble(Double numero, double min, double max, String mensaje) {
        validarNumeroRequerido(numero);

        if (numero < min || numero > max)
            throw new IllegalArgumentException(mensaje);
    }




}
