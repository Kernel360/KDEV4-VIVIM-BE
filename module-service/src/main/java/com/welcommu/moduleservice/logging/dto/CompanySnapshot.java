package com.welcommu.moduleservice.logging.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanySnapshot {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String coOwner;
    private String businessNumber;
    private CompanyRole companyRole;

    public Company toEntity() {
        return Company.builder()
            .id(id)
            .name(name)
            .phone(phone)
            .email(email)
            .address(address)
            .coOwner(coOwner)
            .businessNumber(businessNumber)
            .companyRole(companyRole)
            .build();
    }

    public static CompanySnapshot from(Company company) {
        return new CompanySnapshot(
            company.getId(),
            company.getName(),
            company.getPhone(),
            company.getEmail(),
            company.getAddress(),
            company.getCoOwner(),
            company.getBusinessNumber(),
            company.getCompanyRole()
        );
    }
}
