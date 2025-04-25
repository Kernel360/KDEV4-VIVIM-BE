package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCompanyResponse {
    private Long id;
    private String name;
    private String businessNumber;
    private String email;
    private CompanyRole companyRole;

    public static ProjectCompanyResponse from(Company company) {
        return new ProjectCompanyResponse(
            company.getId(),
            company.getName(),
            company.getBusinessNumber(),
            company.getEmail(),
            company.getCompanyRole()
        );
    }
}
