package com.welcommu.moduledomain.user;

import com.welcommu.moduledomain.company.Company;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime passwordModifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;


    // Company와의 연관 관계를 나타내는 필드
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
