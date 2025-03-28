package com.welcommu.moduleservice.company.dto;

import com.welcommu.moduledomain.company.Company;

public class CompanyRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;

    // Getter, Setter 등 생략

    // DTO를 Entity로 변환하는 toEntity 메서드
    public Company toEntity() {
        return Company.builder()
                .name(this.name)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .build();
    }
}
