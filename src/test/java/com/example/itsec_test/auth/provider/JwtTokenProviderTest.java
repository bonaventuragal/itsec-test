package com.example.itsec_test.auth.provider;

import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.auth.constant.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {
    @Autowired
	private JwtTokenProvider tokenProvider;

	@Test
	void testGenerateTokenContainsUsernameAndRole() {
		User user = new User();
		user.setUsername("testuser");
		user.setRole(UserRole.VIEWER);

		String token = tokenProvider.generateToken(user);
		assertNotNull(token);
	}
}
