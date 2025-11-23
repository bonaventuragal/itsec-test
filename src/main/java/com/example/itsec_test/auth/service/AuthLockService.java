package com.example.itsec_test.auth.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.example.itsec_test.common.provider.KeyValueStoreProvider;

@Service
public class AuthLockService {
    private final KeyValueStoreProvider keyValueStoreProvider;

    private final int MAX_LOGIN_FAILS = 5;
    private final int LOGIN_FAIL_COUNT_EXPIRY_MINUTES = 10;
    private final int LOGIN_LOCK_DURATION_MINUTES = 30;

    public AuthLockService(KeyValueStoreProvider keyValueStoreProvider) {
        this.keyValueStoreProvider = keyValueStoreProvider;
    }

    public boolean isAccountLocked(String usernameOrEmail) {
        String lockKey = generateLoginLockedKeyString(usernameOrEmail);
        Boolean isLocked = (Boolean) this.keyValueStoreProvider.getValue(lockKey);
        return isLocked != null && isLocked;
    }

    public void onLoginFailure(String usernameOrEmail) {
        String failCountKey = generateLoginFailCountKey(usernameOrEmail);
        Integer failCount = (Integer) this.keyValueStoreProvider.getValue(failCountKey);
        if (failCount == null) {
            failCount = 0;
        }
        failCount += 1;
        this.keyValueStoreProvider.saveValue(failCountKey, failCount,
                Duration.ofMinutes(LOGIN_FAIL_COUNT_EXPIRY_MINUTES));

        if (failCount >= MAX_LOGIN_FAILS) {
            String lockKey = generateLoginLockedKeyString(usernameOrEmail);
            this.keyValueStoreProvider.saveValue(lockKey, true, Duration.ofMinutes(LOGIN_LOCK_DURATION_MINUTES));
        }
    }

    private String generateLoginFailCountKey(String usernameOrEmail) {
        return "login_fail_count:" + usernameOrEmail;
    }

    private String generateLoginLockedKeyString(String usernameOrEmail) {
        return "login_locked:" + usernameOrEmail;
    }
}
