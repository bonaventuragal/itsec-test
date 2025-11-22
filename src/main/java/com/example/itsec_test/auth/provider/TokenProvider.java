package com.example.itsec_test.auth.provider;

import com.example.itsec_test.auth.model.User;

public interface TokenProvider {
	String generateToken(User user);
}
