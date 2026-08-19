package com.moises.auth.services;

import java.util.Set;

import com.moises.auth.dto.UsuarioRequest;
import com.moises.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}