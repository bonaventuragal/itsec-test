package com.example.itsec_test.audit.controller;

import com.example.itsec_test.audit.dto.AuditLogResponse;
import com.example.itsec_test.audit.service.AuditLogService;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/audit-logs")
@Tag(name = "AuditLog")
public class AuditLogController {
	private final AuditLogService auditLogService;

	public AuditLogController(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public PaginationResponse<AuditLogResponse> getAuditLogs(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		PaginationRequest paginationRequest = PaginationRequest.builder()
				.page(page)
				.size(size)
				.build();
		return auditLogService.getAllLogs(paginationRequest);
	}
}
