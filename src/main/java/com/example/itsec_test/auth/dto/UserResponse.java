package com.example.itsec_test.auth.dto;

import com.example.itsec_test.auth.constant.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String username;
    private UserRole role;
}
