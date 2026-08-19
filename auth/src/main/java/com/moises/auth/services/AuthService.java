package com.moises.auth.services;

import com.moises.auth.dto.LoginRequest;
import com.moises.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}
