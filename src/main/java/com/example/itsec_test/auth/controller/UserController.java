package com.example.itsec_test.auth.controller;

import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.service.UserService;
import com.example.itsec_test.common.exception.ForbiddenRequestException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest request, HttpServletRequest httpRequest) {
		User user = (User) httpRequest.getAttribute("user");
		if (user == null) {
			throw new ForbiddenRequestException("User not found in request");
		}
		return userService.updateUser(request, user);
	}
}
