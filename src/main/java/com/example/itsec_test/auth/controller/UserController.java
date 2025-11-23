package com.example.itsec_test.auth.controller;

import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.service.UserService;
import com.example.itsec_test.common.dto.MessageResponse;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import com.example.itsec_test.common.exception.ForbiddenRequestException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse updateUser(
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }
        return userService.updateUser(request, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public MessageResponse deleteUser(
            @NonNull @PathVariable Integer id,
            HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }
        userService.deleteUser(id, user);

        return MessageResponse.builder()
                .message("User deleted successfully")
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UserResponse getUserById(@NonNull @PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public PaginationResponse<UserResponse> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .page(page)
                .size(size)
                .build();
        return userService.getUsers(paginationRequest);
    }
}
