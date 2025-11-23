package com.example.itsec_test.common.provider;

import java.time.Duration;

public interface KeyValueStoreProvider {
	void saveValue(String key, Object value, Duration expiry);
	Object getValue(String key);
	void deleteValue(String key);
	void updateExpiry(String key, Duration duration);
}
