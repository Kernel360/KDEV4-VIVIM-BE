package com.welcommu.moduleservice.company.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyModifyRequest {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String coOwner;
    private String businessNumber;
    private CompanyRole companyRole;
    private Long version;

    public Company modifyCompany(Company company) {
        company.setName(this.name);
        company.setAddress(this.address);
        company.setPhone(this.phone);
        company.setEmail(this.email);
        company.setCoOwner(this.coOwner);
        company.setBusinessNumber(this.businessNumber);
        company.setCompanyRole(this.companyRole);
        return company;
    }
}
