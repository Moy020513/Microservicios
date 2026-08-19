package com.moises.commons.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}