package com.welcommu.moduleinfra.company;

import com.welcommu.moduledomain.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<Company> searchByConditions(String name, String businessNumber, String email, Boolean isDeleted, Pageable pageable);
}
