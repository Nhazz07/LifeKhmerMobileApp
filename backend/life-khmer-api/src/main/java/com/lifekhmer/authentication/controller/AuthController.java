package com.lifekhmer.authentication.controller;


import com.lifekhmer.authentication.dto.ApiResponse;
import com.lifekhmer.authentication.dto.request.LoginRequest;
import com.lifekhmer.authentication.dto.request.RegisterRequest;
import com.lifekhmer.authentication.dto.response.LoginResponse;
import com.lifekhmer.authentication.dto.response.RegisterResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.register(request);

        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                true,
                "Account registered successfully",
                registerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        LoginResponse loginResponse = authService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "Account login successfully",
                loginResponse
        );

        return ResponseEntity.ok(response);
    }
}
