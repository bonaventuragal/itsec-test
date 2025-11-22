package com.example.itsec_test.audit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

import com.example.itsec_test.audit.model.AuditLog;
import com.example.itsec_test.audit.repository.AuditLogRepository;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private final AuditLogRepository auditLogRepository;

    public RequestLoggingFilter(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        AuditLog auditLog = new AuditLog();
        auditLog.setMethod(request.getMethod());
        auditLog.setPath(request.getRequestURI());
        auditLog.setClientIp(request.getRemoteAddr());
        auditLog.setUserAgent(request.getHeader("User-Agent"));
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);

        filterChain.doFilter(request, response);
    }
}
