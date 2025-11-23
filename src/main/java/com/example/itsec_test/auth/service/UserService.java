package com.example.itsec_test.auth.service;

import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public UserResponse updateUser(UpdateUserRequest request, User user) {
        Optional<User> userOpt = this.userRepository.findById(request.getId());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found");
        }
        User userToUpdate = userOpt.get();

        if (!user.getRole().equals(UserRole.SUPER_ADMIN)
                && !userToUpdate.getId().equals(user.getId())) {
            throw new ForbiddenRequestException("You do not have permission to update this user");
        }

        userToUpdate.setFullName(request.getFullName());
        if (user.getRole().equals(UserRole.SUPER_ADMIN) && request.getRole() != null) {
            userToUpdate.setRole(request.getRole());
        }
        User updatedUser = this.userRepository.save(userToUpdate);

        return mapToResponse(updatedUser);
    }

    public void deleteUser(@NonNull Integer userId, User requestUser) {
        Optional<User> userOpt = this.userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found");
        }
        User userToDelete = userOpt.get();

        if (!requestUser.getRole().equals(UserRole.SUPER_ADMIN)) {
            if (!userToDelete.getId().equals(requestUser.getId())) {
                throw new ForbiddenRequestException("You do not have permission to delete this user");
            }
        }

        userToDelete.setDeletedAt(LocalDateTime.now());
        this.userRepository.save(userToDelete);
    }

    public UserResponse getUserById(@NonNull Integer id) {
        Optional<User> userOpt = this.userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found");
        }
        User user = userOpt.get();
        return mapToResponse(user);
    }

    public PaginationResponse<UserResponse> getUsers(
            PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                Math.max(paginationRequest.getPage() - 1, 0),
                Math.max(paginationRequest.getSize(), 1));

        Page<User> page = this.userRepository.findAll(pageable);

        return PaginationResponse.<UserResponse>builder()
                .page(paginationRequest.getPage())
                .size(paginationRequest.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .items(page.getContent().stream().map(this::mapToResponse).toList())
                .build();
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
