package com.welcommu.moduledomain.company;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String businessNumber;
    private String address;
    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private CompanyRole companyRole;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

    private String coOwner;

    // 회사 생성 시 필요한 필드에 대해 기본 값을 설정할 수 있도록 처리
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isDeleted == null) {
            isDeleted = false;  // 기본값을 false로 설정
        }
    }
}
