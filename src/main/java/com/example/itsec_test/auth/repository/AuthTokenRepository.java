package com.example.itsec_test.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.itsec_test.auth.model.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
}
