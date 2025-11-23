package com.example.itsec_test.audit.controller;

import com.example.itsec_test.audit.dto.AuditLogResponse;
import com.example.itsec_test.audit.filter.RequestLoggingFilter;
import com.example.itsec_test.audit.service.AuditLogService;
import com.example.itsec_test.auth.filter.JwtUserFilter;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(AuditLogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuditLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestLoggingFilter requestLoggingFilter;

    @MockitoBean
    private JwtUserFilter jwtUserFilter;

    @MockitoBean
    private AuditLogService auditLogService;

    @Test
    void testGetAuditLogs() throws Exception {
	AuditLogResponse log1 = AuditLogResponse.builder()
		.id(1)
		.method("GET")
		.path("/api/test1")
		.clientIp("127.0.0.1")
		.userAgent("JUnit")
		.timestamp(LocalDateTime.now())
		.userId(1)
		.build();
	AuditLogResponse log2 = AuditLogResponse.builder()
		.id(2)
		.method("POST")
		.path("/api/test2")
		.clientIp("127.0.0.2")
		.userAgent("JUnit")
		.timestamp(LocalDateTime.now())
		.userId(null)
		.build();

	PaginationResponse<AuditLogResponse> response = PaginationResponse.<AuditLogResponse>builder()
		.page(1)
		.size(10)
		.totalElements(2)
		.totalPages(1)
		.items(List.of(log1, log2))
		.build();

	when(auditLogService.getAllLogs(any(PaginationRequest.class))).thenReturn(response);

	mockMvc.perform(get("/api/v1/audit-logs?page=1&size=10"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.page").value(1))
		.andExpect(jsonPath("$.size").value(10))
		.andExpect(jsonPath("$.totalElements").value(2))
		.andExpect(jsonPath("$.totalPages").value(1))
		.andExpect(jsonPath("$.items").isArray())
		.andExpect(jsonPath("$.items[0].id").value(log1.getId()))
		.andExpect(jsonPath("$.items[0].method").value(log1.getMethod()))
		.andExpect(jsonPath("$.items[1].id").value(log2.getId()))
		.andExpect(jsonPath("$.items[1].method").value(log2.getMethod()));
    }
}
