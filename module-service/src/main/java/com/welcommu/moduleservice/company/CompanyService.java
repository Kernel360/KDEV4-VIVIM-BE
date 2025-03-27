package com.welcommu.moduleservice.company;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.modulerepository.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company findCompanyByName(String name) {
        Optional<Company> company = companyRepository.findByName(name);
        return company.orElse(null);
    }

    // 다른 필요한 서비스 메서드들...
}