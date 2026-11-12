package com.lifekhmer.accounts.controller;

import com.lifekhmer.accounts.dto.request.UpdateProfileRequest;
import com.lifekhmer.accounts.dto.response.ProfileResponse;
import com.lifekhmer.accounts.service.UserService;
import com.lifekhmer.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> me() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile retrieved", userService.getCurrentProfile())
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> update(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile updated", userService.updateCurrentProfile(request))
        );
    }
}
