package com.example.itsec_test.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.itsec_test.auth.provider.AuthProvider;
import com.example.itsec_test.auth.provider.TokenProvider;

import org.springframework.stereotype.Service;

import com.example.itsec_test.auth.dto.LoginRequest;
import com.example.itsec_test.auth.dto.RegisterRequest;
import com.example.itsec_test.auth.dto.ValidateOtpRequest;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.model.AuthOtp;
import com.example.itsec_test.auth.constant.OtpPurpose;
import com.example.itsec_test.auth.repository.AuthOtpRepository;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.provider.MailProvider;
import com.example.itsec_test.auth.repository.AuthTokenRepository;
import com.example.itsec_test.auth.model.AuthToken;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthOtpRepository authOtpRepository;
    private final AuthProvider authProvider;

    private final AuthTokenRepository authTokenRepository;
    private final TokenProvider tokenProvider;
    private final MailProvider mailProvider;

    private final AuthLockService authLockService;

    public AuthService(
            UserRepository userRepository,
            AuthOtpRepository authOtpRepository,
            AuthProvider authProvider,
            AuthTokenRepository authTokenRepository,
            TokenProvider tokenProvider,
            AuthLockService authLockService,
            MailProvider mailProvider) {
        this.userRepository = userRepository;
        this.authOtpRepository = authOtpRepository;
        this.authProvider = authProvider;
        this.authTokenRepository = authTokenRepository;
        this.tokenProvider = tokenProvider;
        this.authLockService = authLockService;
        this.mailProvider = mailProvider;
    }

    public void register(RegisterRequest request) {
        Optional<User> userOpt = this.userRepository.findByUsernameOrEmail(
            request.getUsername(), request.getEmail());
        if (userOpt.isPresent()) {
            throw new BadRequestException("Username or email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setFullName(request.getFullName());
        newUser.setRole(request.getRole());

        String encodedPassword = this.authProvider.encodePassword(request.getPassword());
        newUser.setPassword(encodedPassword);

        User user = this.userRepository.save(newUser);

        String otp = this.generateOtp();

        AuthOtp authOtp = new AuthOtp();
        authOtp.setOtp(otp);
        authOtp.setUser(user);
        authOtp.setPurpose(OtpPurpose.REGISTER);
        authOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        this.authOtpRepository.save(authOtp);

        this.mailProvider.sendMail(
            user.getEmail(),
            "Verify your account",
            "Your OTP code is: " + otp
        );
    }

    public void login(LoginRequest request) {
        if (this.authLockService.isAccountLocked(request.getUsernameOrEmail())) {
            throw new BadRequestException("Account is locked due to too many failed login attempts. Please try again later.");
        }

        Optional<User> userOpt = this.userRepository.findByUsernameOrEmail(
            request.getUsernameOrEmail(), request.getUsernameOrEmail());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        User user = userOpt.get();
        if (!this.authProvider.matches(request.getPassword(), user.getPassword())) {
            this.authLockService.onLoginFailure(request.getUsernameOrEmail());
            throw new BadRequestException("Invalid password");
        }

        if (!user.getIsVerified()) {
            throw new BadRequestException("User is not verified");
        }

        String otp = this.generateOtp();

        AuthOtp authOtp = new AuthOtp();
        authOtp.setOtp(otp);
        authOtp.setUser(user);
        authOtp.setPurpose(OtpPurpose.LOGIN);
        authOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        this.authOtpRepository.save(authOtp);

        this.mailProvider.sendMail(
            user.getEmail(),
            "Your Login OTP Code",
            "Your OTP code is: " + otp
        );
    }

    public String verifyOtp(ValidateOtpRequest request) {
        Optional<User> userOpt = this.userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found");
        }
        User user = userOpt.get();

        Optional<AuthOtp> authOtpOpt = this.authOtpRepository.findByOtp(request.getOtp());
        if (authOtpOpt.isEmpty()) {
            throw new BadRequestException("Invalid OTP");
        }
        AuthOtp otp = authOtpOpt.get();

        if (!otp.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("OTP does not belong to user");
        }

        if (otp.getVerifiedAt() != null || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired or already used");
        }

        String tokenResponse = null;
        if (otp.getPurpose() == OtpPurpose.REGISTER) {
            user.setIsVerified(true);
            this.userRepository.save(user);
        } else if (otp.getPurpose() == OtpPurpose.LOGIN) {
            String jwt = this.tokenProvider.generateToken(user);
            AuthToken token = new AuthToken();
            token.setUser(user);
            token.setToken(jwt);
            token.setExpiresAt(LocalDateTime.now().plusHours(1));
            token.setIsActive(true);
            this.authTokenRepository.save(token);

            tokenResponse = jwt;
        }

        otp.setVerifiedAt(LocalDateTime.now());
        this.authOtpRepository.save(otp);

        return tokenResponse;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otpInt = 100000 + random.nextInt(900000);
        return String.valueOf(otpInt);
    }
}
