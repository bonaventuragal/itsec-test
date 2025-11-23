package com.example.itsec_test.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenRequestException extends ResponseStatusException {
	public ForbiddenRequestException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}
}
