package com.welcommu.moduledomain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue
    private Long id;
    private String email;             // 요청자 email
    private String token;             // 랜덤 코드
    private LocalDateTime expiresAt;  // 만료 시각

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
