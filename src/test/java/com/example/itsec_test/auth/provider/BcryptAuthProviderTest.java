package com.example.itsec_test.auth.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BcryptAuthProviderTest {
	private BcryptAuthProvider provider;

	@BeforeEach
	void setUp() {
		provider = new BcryptAuthProvider();
	}

	@Test
	void testEncodePasswordAndMatch() {
		String rawPassword = "password";
		String encoded = provider.encodePassword(rawPassword);
		assertTrue(provider.matches(rawPassword, encoded));
	}

	@Test
	void testDoesNotMatchWrongPassword() {
		String rawPassword = "password";
		String encoded = provider.encodePassword(rawPassword);
		assertFalse(provider.matches("wrongPassword", encoded));
	}
}
