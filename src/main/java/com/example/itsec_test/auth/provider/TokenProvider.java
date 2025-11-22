package com.example.itsec_test.auth.provider;

import com.example.itsec_test.auth.model.User;
import io.jsonwebtoken.Claims;

public interface TokenProvider {
	String generateToken(User user);
	Claims parseClaims(String token);
}
