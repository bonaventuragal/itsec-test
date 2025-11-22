package com.example.itsec_test.auth.dto;

import com.example.itsec_test.auth.constant.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private UserRole role;
}