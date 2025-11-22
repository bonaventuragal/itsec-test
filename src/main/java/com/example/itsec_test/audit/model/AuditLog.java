package com.example.itsec_test.audit.model;

import java.time.LocalDateTime;

import com.example.itsec_test.common.model.BaseModel;
import com.example.itsec_test.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Entity
public class AuditLog extends BaseModel {
    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String path;

    @Column(nullable = true)
    private String requestParams;

    @Column(nullable = true)
    private String requestBody;

    @Column(nullable = true)
    private String clientIp;

    @Column(nullable = true)
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
