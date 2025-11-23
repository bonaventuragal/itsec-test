package com.example.itsec_test.auth.model;

import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.example.itsec_test.audit.model.AuditLog;
import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.common.model.BaseMutableModel;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "auditLogs", "authTokens", "authOtps" })
@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseMutableModel {
    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthToken> authTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthOtp> authOtps;
}
