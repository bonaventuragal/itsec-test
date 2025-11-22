package com.example.itsec_test.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateOtpResponse {
    private String token;
}
