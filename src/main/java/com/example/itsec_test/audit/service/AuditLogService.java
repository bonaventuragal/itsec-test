package com.example.itsec_test.audit.service;

import com.example.itsec_test.audit.model.AuditLog;
import com.example.itsec_test.audit.repository.AuditLogRepository;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import com.example.itsec_test.audit.dto.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public PaginationResponse<AuditLogResponse> getAllLogs(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                Math.max(paginationRequest.getPage() - 1, 0),
                Math.max(paginationRequest.getSize(), 1),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLog> page = auditLogRepository.findAll(pageable);

        return PaginationResponse.<AuditLogResponse>builder()
                .page(paginationRequest.getPage())
                .size(paginationRequest.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .items(page.getContent().stream().map(this::mapToResponse).toList())
                .build();
    }

    private AuditLogResponse mapToResponse(AuditLog log) {
        User user = log.getUser();

        return AuditLogResponse.builder()
                .id(log.getId())
                .method(log.getMethod())
                .path(log.getPath())
                .clientIp(log.getClientIp())
                .userAgent(log.getUserAgent())
                .timestamp(log.getCreatedAt())
                .userId(user != null ? user.getId() : null)
                .build();
    }
}
