package com.example.itsec_test.user.model;

import java.util.List;

import com.example.itsec_test.audit.model.AuditLog;
import com.example.itsec_test.auth.model.AuthOtp;
import com.example.itsec_test.auth.model.AuthToken;
import com.example.itsec_test.common.model.BaseMutableModel;
import com.example.itsec_test.user.constant.UserRole;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "auditLogs", "authTokens", "authOtps" })
@Entity
@Table(name = "users")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthToken> authTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthOtp> authOtps;
}
