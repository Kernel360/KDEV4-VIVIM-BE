package com.welcommu.moduleinfra.company;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {

    Optional<Company> findByName(String name);

    Optional<Company> findByBusinessNumber(String businessNumber);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByPhone(String phone);

    List<Company> findByCompanyRole(CompanyRole companyRole);
}