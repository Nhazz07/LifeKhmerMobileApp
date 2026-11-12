package com.lifekhmer.authentication.service;

import com.lifekhmer.authentication.dto.request.LoginRequest;
import com.lifekhmer.authentication.dto.request.RegisterRequest;
import com.lifekhmer.authentication.dto.response.LoginResponse;
import com.lifekhmer.authentication.dto.response.RegisterResponse;
import com.lifekhmer.authentication.dto.request.RefreshTokenRequest;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
    LoginResponse refresh(RefreshTokenRequest request);
    void logout(String refreshToken);
}
