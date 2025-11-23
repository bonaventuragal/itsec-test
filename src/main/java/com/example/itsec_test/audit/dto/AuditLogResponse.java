package com.example.itsec_test.audit.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogResponse {
    private Integer id;
    private String method;
    private String path;
    private String clientIp;
    private String userAgent;
    private LocalDateTime timestamp;
    private Integer userId;
}
