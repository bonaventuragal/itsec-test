package com.example.itsec_test.common.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@SuppressWarnings("null")
@Component
public class RedisStoreProvider implements KeyValueStoreProvider {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void saveValue(String key, Object value, Duration expiry) {
		if (expiry != null) {
			redisTemplate.opsForValue().set(key, value, expiry);
		} else {
			redisTemplate.opsForValue().set(key, value);
		}
	}

	@Override
	public Object getValue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void updateExpiry(String key, Duration duration) {
		redisTemplate.expire(key, duration);
	}
}
