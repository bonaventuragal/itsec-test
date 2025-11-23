package com.example.itsec_test.auth.service;

import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.UpdateUserRequest;
import com.example.itsec_test.auth.dto.UserResponse;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateUserSuccessBySelf() {
        User user = new User();
        user.setId(1);
        user.setFullName("Old Name");
        user.setEmail("user@gmail.com");
        user.setUsername("user1");
        user.setRole(UserRole.CONTRIBUTOR);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setFullName("New Name");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(request, user);

        assertNotNull(response);
        assertEquals(request.getFullName(), response.getFullName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getUsername(), response.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateUserSuccessBySuperAdmin() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setFullName("Old Name");
        userToUpdate.setEmail("user2@gmail.com");
        userToUpdate.setUsername("user2");
        userToUpdate.setRole(UserRole.CONTRIBUTOR);

        User superAdmin = new User();
        superAdmin.setId(2);
        superAdmin.setRole(UserRole.SUPER_ADMIN);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setFullName("Admin Updated Name");
        request.setRole(UserRole.EDITOR);

        when(userRepository.findById(1)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(request, superAdmin);

        assertNotNull(response);
        assertEquals(request.getFullName(), response.getFullName());
        assertEquals(request.getRole(), response.getRole());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserForbiddenForNonSelf() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setFullName("Old Name");
        userToUpdate.setRole(UserRole.CONTRIBUTOR);

        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setRole(UserRole.CONTRIBUTOR);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setFullName("New Name");
        request.setRole(UserRole.CONTRIBUTOR);

        when(userRepository.findById(1)).thenReturn(Optional.of(userToUpdate));
        assertThrows(ForbiddenRequestException.class,
                () -> userService.updateUser(request, otherUser));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.SUPER_ADMIN);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(2);
        request.setFullName("Name");
        request.setRole(UserRole.EDITOR);

        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> userService.updateUser(request, user));
        verify(userRepository, times(1)).findById(2);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteUserSuccessBySelf() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.VIEWER);

        User userToDelete = new User();
        userToDelete.setId(1);
        userToDelete.setRole(UserRole.VIEWER);

        when(userRepository.findById(1)).thenReturn(Optional.of(userToDelete));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.deleteUser(1, user);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteUserSuccessBySuperAdmin() {
        User superAdmin = new User();
        superAdmin.setId(1);
        superAdmin.setRole(UserRole.SUPER_ADMIN);

        User userToDelete = new User();
        userToDelete.setId(2);
        userToDelete.setRole(UserRole.VIEWER);

        when(userRepository.findById(2)).thenReturn(Optional.of(userToDelete));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.deleteUser(2, superAdmin);
        verify(userRepository, times(1)).findById(2);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserForbiddenForNonSelf() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.EDITOR);

        User userToDelete = new User();
        userToDelete.setId(2);
        userToDelete.setRole(UserRole.VIEWER);

        when(userRepository.findById(2)).thenReturn(Optional.of(userToDelete));

        assertThrows(ForbiddenRequestException.class,
                () -> userService.deleteUser(2, user));
        verify(userRepository, times(1)).findById(2);
    }

    @Test
    void testDeleteUserNotFound() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.SUPER_ADMIN);

        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> userService.deleteUser(2, user));
        verify(userRepository, times(1)).findById(2);
    }
}
