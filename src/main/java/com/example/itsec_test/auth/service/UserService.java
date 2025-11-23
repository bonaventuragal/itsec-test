package com.example.itsec_test.auth.service;

import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public UserResponse updateUser(UpdateUserRequest request, User user) {
        Optional<User> userOpt = userRepository.findById(request.getId());
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
        User updatedUser = userRepository.save(userToUpdate);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .username(updatedUser.getUsername())
                .role(updatedUser.getRole())
                .build();
    }
}
