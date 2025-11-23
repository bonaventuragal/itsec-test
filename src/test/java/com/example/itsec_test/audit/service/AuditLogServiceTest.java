package com.example.itsec_test.audit.service;

import com.example.itsec_test.audit.model.AuditLog;
import com.example.itsec_test.audit.repository.AuditLogRepository;
import com.example.itsec_test.audit.dto.AuditLogResponse;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import com.example.itsec_test.auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {
	private AuditLogRepository auditLogRepository;
	private AuditLogService auditLogService;

	@BeforeEach
	void setUp() {
		auditLogRepository = mock(AuditLogRepository.class);
		auditLogService = new AuditLogService(auditLogRepository);
	}

    @SuppressWarnings("null")
	@Test
	void testGetAllLogs() {
		User user = new User();
		user.setId(1);
		user.setFullName("User Name");

		AuditLog log1 = new AuditLog();
		log1.setId(1);
		log1.setMethod("GET");
		log1.setPath("/api/test1");
		log1.setClientIp("127.0.0.1");
		log1.setUserAgent("JUnit");
		log1.setCreatedAt(LocalDateTime.now());
		log1.setUser(user);

		AuditLog log2 = new AuditLog();
		log2.setId(2);
		log2.setMethod("POST");
		log2.setPath("/api/test2");
		log2.setClientIp("127.0.0.1");
		log2.setUserAgent("JUnit");
		log2.setCreatedAt(LocalDateTime.now());

		Page<AuditLog> page = new PageImpl<>(List.of(log1, log2));
		when(auditLogRepository.findAll(any(Pageable.class))).thenReturn(page);

		PaginationRequest paginationRequest = PaginationRequest.builder()
				.page(1)
				.size(10)
				.build();
		PaginationResponse<AuditLogResponse> response = auditLogService.getAllLogs(paginationRequest);

		assertNotNull(response);
		assertEquals(2, response.getItems().size());
		assertEquals(log1.getId(), response.getItems().get(0).getId());
		assertEquals(log2.getId(), response.getItems().get(1).getId());
		assertEquals(log1.getMethod(), response.getItems().get(0).getMethod());
		assertEquals(log2.getMethod(), response.getItems().get(1).getMethod());
		assertEquals(user.getId(), response.getItems().get(0).getUserId());
		assertEquals(null, response.getItems().get(1).getUserId());
		verify(auditLogRepository, times(1)).findAll(any(Pageable.class));
	}
}
