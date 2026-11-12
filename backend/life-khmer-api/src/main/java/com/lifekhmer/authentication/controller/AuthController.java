package com.lifekhmer.authentication.controller;


import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.authentication.dto.request.LoginRequest;
import com.lifekhmer.authentication.dto.request.RegisterRequest;
import com.lifekhmer.authentication.dto.response.LoginResponse;
import com.lifekhmer.authentication.dto.response.RegisterResponse;
import com.lifekhmer.authentication.dto.request.RefreshTokenRequest;
import com.lifekhmer.authentication.dto.request.LogoutRequest;
import com.lifekhmer.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.register(request);

        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                true,
                "Account registered successfully",
                registerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        LoginResponse loginResponse = authService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "Account login successfully",
                loginResponse
        );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh") public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) { return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed", authService.refresh(request))); }
    @PostMapping("/logout") public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) { authService.logout(request.refreshToken()); return ResponseEntity.noContent().build(); }
}
