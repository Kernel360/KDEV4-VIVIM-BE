package com.welcommu.moduleservice.company.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String coOwner;
    private String businessNumber;
    private CompanyRole companyRole;

    // DTO를 Entity로 변환하는 toEntity 메서드
    public Company toEntity() {
        return Company.builder()
                .name(this.name)
                .address(this.address)
                .phone(this.phone)
                .email(this.email)
                .coOwner(this.coOwner)
                .companyRole(this.companyRole)
                .businessNumber(this.businessNumber)
                .isDeleted(false)
                .build();
    }
}
