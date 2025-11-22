package com.example.itsec_test.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.core.MethodParameter;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationExceptionConfigTest {
	private ValidationExceptionConfig config;

	@org.junit.jupiter.api.BeforeEach
	void setUp() {
		config = new ValidationExceptionConfig();
	}

	@Test
	void testHandleValidationException() {
		BindingResult bindingResult = mock(BindingResult.class);
		FieldError error1 = new FieldError("objectName", "field1", "must not be null");
		FieldError error2 = new FieldError("objectName", "field2", "must be positive");
		when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(error1, error2));

		MethodParameter methodParameter = mock(MethodParameter.class);
		when(methodParameter.getParameterName()).thenReturn("request");

		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		when(exception.getBindingResult()).thenReturn(bindingResult);
		when(exception.getParameter()).thenReturn(methodParameter);

		ResponseEntity<Map<String, Object>> response = config.handleValidationException(exception);

		assertEquals(400, response.getStatusCode().value());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals(400, body.get("status"));
		assertEquals("Bad Request", body.get("error"));
		assertEquals("request", body.get("path"));
		assertEquals(Arrays.asList(
            "field1: must not be null",
			"field2: must be positive"
		), body.get("messages"));
	}
}
