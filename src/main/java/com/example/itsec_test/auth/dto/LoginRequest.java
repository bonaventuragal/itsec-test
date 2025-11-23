package com.example.itsec_test.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String password;
}
