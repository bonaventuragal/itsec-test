package com.example.itsec_test.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.itsec_test.auth.provider.AuthProvider;

import org.springframework.stereotype.Service;

import com.example.itsec_test.auth.dto.RegisterRequest;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.model.AuthOtp;
import com.example.itsec_test.auth.constant.OtpPurpose;
import com.example.itsec_test.auth.repository.AuthOtpRepository;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.exception.BadRequestException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthOtpRepository authOtpRepository;
    private final AuthProvider authProvider;

    public AuthService(
            UserRepository userRepository,
            AuthOtpRepository authOtpRepository,
            AuthProvider authProvider) {
        this.userRepository = userRepository;
        this.authOtpRepository = authOtpRepository;
        this.authProvider = authProvider;
    }

    public void register(RegisterRequest request) {
        Optional<User> user = this.userRepository.findByUsernameOrEmail(
            request.getUsername(), request.getEmail());
        if (user.isPresent()) {
            throw new BadRequestException("Username or email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setFullName(request.getFullName());
        newUser.setRole(request.getRole());

        String encodedPassword = this.authProvider.encodePassword(request.getPassword());
        newUser.setPassword(encodedPassword);

        User savedUser = this.userRepository.save(newUser);

        String otp = this.generateOtp();

        AuthOtp authOtp = new AuthOtp();
        authOtp.setOtp(otp);
        authOtp.setUser(savedUser);
        authOtp.setPurpose(OtpPurpose.REGISTER);
        authOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        this.authOtpRepository.save(authOtp);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otpInt = 100000 + random.nextInt(900000);
        return String.valueOf(otpInt);
    }
}
