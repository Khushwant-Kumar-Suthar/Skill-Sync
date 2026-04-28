package com.skillsync.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.skillsync.auth.dto.AuthResponseDto;
import com.skillsync.auth.dto.LoginRequest;
import com.skillsync.auth.dto.RegisterRequest;
import com.skillsync.auth.service.AuthService;
import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // FIX: @Valid added — without it the @NotBlank/@Email/@Size annotations
    // on RegisterRequest are never evaluated and invalid data passes through.
    @PostMapping("/register")
    public ApiResponse<Void> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
        return ResponseUtil.successMessage("User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponseDto response = authService.login(request);
        return ResponseUtil.success("Login successful", response);
    }
}