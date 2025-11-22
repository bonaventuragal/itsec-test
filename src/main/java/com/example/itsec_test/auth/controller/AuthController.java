package com.example.itsec_test.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itsec_test.auth.dto.RegisterRequest;
import com.example.itsec_test.auth.dto.ValidateOtpRequest;
import com.example.itsec_test.auth.dto.ValidateOtpResponse;
import com.example.itsec_test.auth.dto.LoginRequest;
import com.example.itsec_test.auth.service.AuthService;
import com.example.itsec_test.common.dto.MessageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public MessageResponse register(@Valid @RequestBody RegisterRequest request) {
        this.authService.register(request);
        return MessageResponse.builder()
                .message("Registration successful. Please check your email for completing the registration.")
                .build();
    }

    @PostMapping("/login")
    public MessageResponse login(@Valid @RequestBody LoginRequest request) {
        this.authService.login(request);
        return MessageResponse.builder()
                .message("Please check your email to complete login.")
                .build();
    }
    @PostMapping("/validate-otp")
    public ValidateOtpResponse validateOtp(@Valid @RequestBody ValidateOtpRequest request) {
        String token = this.authService.verifyOtp(request);
        return ValidateOtpResponse.builder()
                .token(token)
                .build();
    }
}
