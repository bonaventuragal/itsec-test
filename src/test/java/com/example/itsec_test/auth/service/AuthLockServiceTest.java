package com.example.itsec_test.auth.service;

import com.example.itsec_test.common.provider.KeyValueStoreProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthLockServiceTest {
	private KeyValueStoreProvider keyValueStoreProvider;
	private AuthLockService authLockService;

	@BeforeEach
	void setUp() {
		keyValueStoreProvider = mock(KeyValueStoreProvider.class);
		authLockService = new AuthLockService(keyValueStoreProvider);
	}

	@Test
	void testIsAccountLockedTrue() {
		when(keyValueStoreProvider.getValue("login_locked:user1")).thenReturn(true);
		assertTrue(authLockService.isAccountLocked("user1"));
	}

	@Test
	void testIsAccountLockedFalse() {
		when(keyValueStoreProvider.getValue("login_locked:user1")).thenReturn(false);
		assertFalse(authLockService.isAccountLocked("user1"));
	}

	@Test
	void testIsAccountLockedNull() {
		when(keyValueStoreProvider.getValue("login_locked:user1")).thenReturn(null);
		assertFalse(authLockService.isAccountLocked("user1"));
	}

	@Test
	void testOnLoginFailureFirstTime() {
		when(keyValueStoreProvider.getValue("login_fail_count:user1")).thenReturn(null);
		authLockService.onLoginFailure("user1");
		verify(keyValueStoreProvider).saveValue(eq("login_fail_count:user1"), eq(1), eq(Duration.ofMinutes(1)));
		verify(keyValueStoreProvider, never()).saveValue(eq("login_locked:user1"), eq(true), any());
	}

	@Test
	void testOnLoginFailureIncrement() {
		when(keyValueStoreProvider.getValue("login_fail_count:user1")).thenReturn(2);
		authLockService.onLoginFailure("user1");
		verify(keyValueStoreProvider).saveValue(eq("login_fail_count:user1"), eq(3), eq(Duration.ofMinutes(1)));
		verify(keyValueStoreProvider, never()).saveValue(eq("login_locked:user1"), eq(true), any());
	}

	@Test
	void testOnLoginFailureLockTriggered() {
		when(keyValueStoreProvider.getValue("login_fail_count:user1")).thenReturn(4);
		authLockService.onLoginFailure("user1");
		verify(keyValueStoreProvider).saveValue(eq("login_fail_count:user1"), eq(5), eq(Duration.ofMinutes(1)));
		verify(keyValueStoreProvider).saveValue(eq("login_locked:user1"), eq(true), eq(Duration.ofMinutes(1)));
	}
}
