package com.example.itsec_test.auth.controller;

import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.service.UserService;
import com.example.itsec_test.auth.filter.JwtUserFilter;
import com.example.itsec_test.audit.filter.RequestLoggingFilter;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestLoggingFilter requestLoggingFilter;

    @MockitoBean
    private JwtUserFilter jwtUserFilter;

    @MockitoBean
    private UserService userService;

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("User Name");
        user.setEmail("user@example.com");
        user.setUsername("user1");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setFullName("Updated Name");
        request.setRole(null);

        UserResponse response = UserResponse.builder()
                .id(1)
                .fullName("Updated Name")
                .email("user@example.com")
                .username("user1")
                .role(null)
                .build();

        when(userService.updateUser(any(UpdateUserRequest.class), any(User.class))).thenReturn(response);

        String json = "{" +
                "\"id\":" + request.getId() + "," +
                "\"fullName\":\"" + request.getFullName() + "\"," +
                "\"role\":null}";

        mockMvc.perform(put("/api/v1/users")
                .requestAttr("user", user)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.fullName").value(response.getFullName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.username").value(response.getUsername()));
    }
}
