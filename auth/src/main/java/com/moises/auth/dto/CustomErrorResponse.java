package com.moises.auth.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}