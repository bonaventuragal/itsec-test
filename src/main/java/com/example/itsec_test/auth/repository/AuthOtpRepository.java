package com.example.itsec_test.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itsec_test.auth.model.AuthOtp;

import java.util.Optional;

public interface AuthOtpRepository extends JpaRepository<AuthOtp, Integer> {
	Optional<AuthOtp> findByOtp(String otp);
}
