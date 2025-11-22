package com.example.itsec_test.auth.model;

import java.time.LocalDateTime;

import com.example.itsec_test.common.model.BaseModel;
import com.example.itsec_test.user.model.User;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Entity
@Table(name = "auth_otp")
public class AuthOtp extends BaseModel {
    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = true)
    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
