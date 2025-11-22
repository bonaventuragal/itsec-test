package com.example.itsec_test.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidateOtpRequest {
    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String otp;
}
