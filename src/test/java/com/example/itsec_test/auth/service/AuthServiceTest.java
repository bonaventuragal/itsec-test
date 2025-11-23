package com.example.itsec_test.auth.service;

import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.dto.LoginRequest;
import com.example.itsec_test.auth.dto.RegisterRequest;
import com.example.itsec_test.auth.model.AuthOtp;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.dto.ValidateOtpRequest;
import com.example.itsec_test.auth.constant.OtpPurpose;
import com.example.itsec_test.auth.model.AuthToken;
import com.example.itsec_test.auth.provider.AuthProvider;
import com.example.itsec_test.auth.provider.TokenProvider;
import com.example.itsec_test.auth.repository.AuthOtpRepository;
import com.example.itsec_test.auth.repository.AuthTokenRepository;
import com.example.itsec_test.auth.repository.UserRepository;
import com.example.itsec_test.common.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
	private UserRepository userRepository;
	private AuthOtpRepository authOtpRepository;
	private AuthTokenRepository authTokenRepository;
	private AuthProvider authProvider;
    private TokenProvider tokenProvider;
	private AuthService authService;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		authOtpRepository = mock(AuthOtpRepository.class);
		authTokenRepository = mock(AuthTokenRepository.class);
		authProvider = mock(AuthProvider.class);
		tokenProvider = mock(TokenProvider.class);
		authService = new AuthService(
            userRepository, authOtpRepository, authProvider, authTokenRepository, tokenProvider);
	}

	@SuppressWarnings("null")
    @Test
	void testRegisterSuccess() {
		when(userRepository.findByUsernameOrEmail("user1", "user1@gmail.com")).thenReturn(Optional.empty());
		when(authProvider.encodePassword("password")).thenReturn("hashed");
    
		User savedUser = new User();
		savedUser.setUsername("user1");
		savedUser.setEmail("user1@gmail.com");
        savedUser.setFullName("User One");
        savedUser.setRole(UserRole.VIEWER);
		savedUser.setPassword("hashed");

		when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterRequest request = new RegisterRequest();
		request.setUsername("user1");
		request.setEmail("user1@gmail.com");
		request.setFullName("User One");
		request.setRole(UserRole.VIEWER);
		request.setPassword("password");

		authService.register(request);

		verify(userRepository, times(1)).save(any(User.class));
		verify(authOtpRepository, times(1)).save(any(AuthOtp.class));
	}

	@SuppressWarnings("null")
    @Test
	void testRegisterDuplicate() {
		when(userRepository.findByUsernameOrEmail("user1", "user1@gmail.com")).thenReturn(Optional.of(new User()));

        RegisterRequest request = new RegisterRequest();
		request.setUsername("user1");
		request.setEmail("user1@gmail.com");

		assertThrows(BadRequestException.class, () -> authService.register(request));
		verify(userRepository, never()).save(any(User.class));
		verify(authOtpRepository, never()).save(any(AuthOtp.class));
	}

	@SuppressWarnings("null")
    @Test
	void testLoginSuccess() {
		User user = new User();
		user.setUsername("user1");
		user.setEmail("user1@gmail.com");
		user.setPassword("hashed");
		user.setIsVerified(true);

		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authProvider.matches("password", "hashed")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user1");
        request.setPassword("password");

		authService.login(request);

		verify(authOtpRepository, times(1)).save(any(AuthOtp.class));
	}

	@SuppressWarnings("null")
    @Test
	void testLoginUserNotFound() {
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user1");
        request.setPassword("password");

		assertThrows(BadRequestException.class, () -> authService.login(request));
		verify(authOtpRepository, never()).save(any(AuthOtp.class));
	}

	@SuppressWarnings("null")
    @Test
	void testLoginInvalidPassword() {
		User user = new User();
		user.setPassword("hashed");
		user.setIsVerified(true);
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authProvider.matches("password", "hashed")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user1");
        request.setPassword("password");

		assertThrows(BadRequestException.class, () -> authService.login(request));
		verify(authOtpRepository, never()).save(any(AuthOtp.class));
	}

	@SuppressWarnings("null")
    @Test
	void testLoginNotVerified() {
		User user = new User();
		user.setPassword("hashed");
		user.setIsVerified(false);
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authProvider.matches("password", "hashed")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user1");
        request.setPassword("password");
        
		assertThrows(BadRequestException.class, () -> authService.login(request));
		verify(authOtpRepository, never()).save(any(AuthOtp.class));
	}

	@Test
	void testVerifyOtpRegisterSuccess() {
		User user = new User();
		user.setId(1);
		user.setUsername("user1");
		user.setIsVerified(false);

		AuthOtp otp = new AuthOtp();
		otp.setOtp("123456");
		otp.setUser(user);
		otp.setPurpose(OtpPurpose.REGISTER);
		otp.setExpiresAt(LocalDateTime.now().plusHours(1));
        
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.of(otp));

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		String result = authService.verifyOtp(request);
		assertNull(result);
		verify(userRepository, times(1)).save(user);
		verify(authOtpRepository, times(1)).save(otp);
	}

	@SuppressWarnings("null")
	@Test
	void testVerifyOtpLoginSuccess() {
		User user = new User();
		user.setId(1);
		user.setUsername("user1");
		user.setIsVerified(true);

		AuthOtp otp = new AuthOtp();
		otp.setOtp("123456");
		otp.setUser(user);
		otp.setPurpose(OtpPurpose.LOGIN);
		otp.setExpiresAt(LocalDateTime.now().plusHours(1));

		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.of(otp));
		when(tokenProvider.generateToken(user)).thenReturn("jwt-token");

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		String result = authService.verifyOtp(request);
		assertEquals("jwt-token", result);
		verify(authTokenRepository, times(1)).save(any(AuthToken.class));
		verify(authOtpRepository, times(1)).save(otp);
	}

	@Test
	void testVerifyOtpUserNotFound() {
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.empty());

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		assertThrows(BadRequestException.class, () -> authService.verifyOtp(request));
	}

	@Test
	void testVerifyOtpInvalidOtp() {
		User user = new User();
		user.setId(1);
		user.setUsername("user1");
        
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.empty());

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		assertThrows(BadRequestException.class, () -> authService.verifyOtp(request));
	}

	@Test
	void testVerifyOtpDoesNotBelongToUser() {
		User user1 = new User();
		user1.setId(1);
        user1.setUsername("user1");

		User user2 = new User();
		user2.setId(2);
        user2.setUsername("user2");

		AuthOtp otp = new AuthOtp();
		otp.setOtp("123456");
		otp.setUser(user2);

		otp.setPurpose(OtpPurpose.REGISTER);
		otp.setExpiresAt(LocalDateTime.now().plusHours(1));
        
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user1));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.of(otp));

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		assertThrows(BadRequestException.class, () -> authService.verifyOtp(request));
	}

	@Test
	void testVerifyOtpExpired() {
		User user = new User();
		user.setId(1);
		user.setUsername("user1");

		AuthOtp otp = new AuthOtp();
		otp.setOtp("123456");
		otp.setUser(user);
		otp.setPurpose(OtpPurpose.REGISTER);
		otp.setExpiresAt(LocalDateTime.now().minusHours(1));
        
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.of(otp));

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		assertThrows(BadRequestException.class, () -> authService.verifyOtp(request));
	}

    @Test
	void testVerifyOtpAlreadyUsed() {
		User user = new User();
		user.setId(1);
		user.setUsername("user1");

		AuthOtp otp = new AuthOtp();
		otp.setOtp("123456");
		otp.setUser(user);
		otp.setPurpose(OtpPurpose.REGISTER);
		otp.setExpiresAt(LocalDateTime.now().plusHours(1));
		otp.setVerifiedAt(LocalDateTime.now().minusHours(1));
        
		when(userRepository.findByUsernameOrEmail("user1", "user1")).thenReturn(Optional.of(user));
		when(authOtpRepository.findByOtp("123456")).thenReturn(Optional.of(otp));

		ValidateOtpRequest request = new ValidateOtpRequest();
		request.setUsernameOrEmail("user1");
		request.setOtp("123456");

		assertThrows(BadRequestException.class, () -> authService.verifyOtp(request));
	}
}
