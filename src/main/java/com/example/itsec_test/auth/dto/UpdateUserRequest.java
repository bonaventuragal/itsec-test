package com.example.itsec_test.auth.dto;

import com.example.itsec_test.auth.constant.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotNull
    private Integer id;

    @NotNull
    private String fullName;

    private UserRole role;
}
