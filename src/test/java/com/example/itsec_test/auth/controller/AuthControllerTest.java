package com.example.itsec_test.auth.controller;

import com.example.itsec_test.auth.dto.RegisterRequest;
import com.example.itsec_test.audit.RequestLoggingFilter;
import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.LoginRequest;
import com.example.itsec_test.auth.dto.ValidateOtpRequest;
import com.example.itsec_test.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestLoggingFilter requestLoggingFilter;

    @MockitoBean
    private AuthService authService;

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setEmail("user1@gmail.com");
        request.setFullName("User One");
        request.setRole(UserRole.VIEWER);
        request.setPassword("password");

        doNothing().when(authService).register(any(RegisterRequest.class));

        String json = "{\"username\":\"user1\",\"email\":\"user1@gmail.com\",\"fullName\":\"User One\",\"role\":\"VIEWER\",\"password\":\"password\"}";

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful. Please check your email for completing the registration."));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user1");
        request.setPassword("password");

        doNothing().when(authService).login(any(LoginRequest.class));

        String json = "{\"usernameOrEmail\":\"user1\",\"password\":\"password\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Please check your email to complete login."));
    }

    @Test
    void testValidateOtp() throws Exception {
        ValidateOtpRequest request = new ValidateOtpRequest();
        request.setUsernameOrEmail("user1");
        request.setOtp("123456");

        when(authService.verifyOtp(any(ValidateOtpRequest.class))).thenReturn("jwt-token");

        String json = "{\"usernameOrEmail\":\"user1\",\"otp\":\"123456\"}";

        mockMvc.perform(post("/api/v1/auth/validate-otp")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
