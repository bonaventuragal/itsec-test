package com.example.itsec_test.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itsec_test.audit.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
    
}
