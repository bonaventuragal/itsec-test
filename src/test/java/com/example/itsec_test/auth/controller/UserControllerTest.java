package com.example.itsec_test.auth.controller;

import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.service.UserService;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        user.setEmail("user@gmail.com");
        user.setUsername("user1");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setFullName("Updated Name");
        request.setRole(null);

        UserResponse response = UserResponse.builder()
                .id(1)
                .fullName("Updated Name")
                .email("user@gmail.com")
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

    @Test
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("User Name");
        user.setEmail("user@gmail.com");
        user.setUsername("user1");

        doNothing().when(userService).deleteUser(eq(1), any(User.class));

        mockMvc.perform(delete("/api/v1/users/1")
                .requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(1)
                .fullName("User Name")
                .email("user@gmail.com")
                .username("user1")
                .role(UserRole.EDITOR)
                .build();

        when(userService.getUserById(1)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.fullName").value(response.getFullName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.role").value(response.getRole().name()));
    }

    @Test
    void testGetUsers() throws Exception {
        UserResponse user1 = UserResponse.builder()
                .id(1)
                .fullName("User One")
                .email("one@example.com")
                .username("userone")
                .role(UserRole.EDITOR)
                .build();
        UserResponse user2 = UserResponse.builder()
                .id(2)
                .fullName("User Two")
                .email("two@example.com")
                .username("usertwo")
                .role(UserRole.CONTRIBUTOR)
                .build();

        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
                .page(1)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .items(java.util.List.of(user1, user2))
                .build();

        when(userService.getUsers(any(PaginationRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/users?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(user1.getId()))
                .andExpect(jsonPath("$.items[0].fullName").value(user1.getFullName()))
                .andExpect(jsonPath("$.items[1].id").value(user2.getId()))
                .andExpect(jsonPath("$.items[1].fullName").value(user2.getFullName()));
    }
}
