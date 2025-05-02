package com.welcommu.moduleservice.company;


import com.welcommu.moduledomain.company.Company;

import com.welcommu.moduleservice.company.dto.CompanyModifyRequest;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.user.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    void createCompany(CompanyRequest companyRequest, Long userId);
    List<Company> getAllCompany();
    Optional<Company> getCompanyById(Long id);
    List<UserResponse> getEmployeesByCompany(Long companyId);
    Company modifyCompany(Long id, CompanyModifyRequest request, Long modifierId);
    void deleteCompany(Long id, Long deleterId);
    Page<CompanyResponse> searchCompanies(String name, String businessNumber, String email, Boolean isDeleted, Pageable pageable);
    void softDeleteCompany(Long id, Long actorId);
}
