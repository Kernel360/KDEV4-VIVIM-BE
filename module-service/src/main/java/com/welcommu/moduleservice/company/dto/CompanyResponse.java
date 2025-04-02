package com.welcommu.moduleservice.company.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CompanyResponse {

    private Long id;
    private String name;
    private String businessNumber;
    private String address;
    private String phone;
    private String email;
    private CompanyRole companyRole;
    private String coOwner;

    // Company 엔티티를 DTO로 변환하는 메서드
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getBusinessNumber(),
                company.getAddress(),
                company.getPhone(),
                company.getEmail(),
                company.getCompanyRole(),
                company.getCoOwner()
        );
    }
}
