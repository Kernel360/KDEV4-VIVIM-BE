package com.welcommu.modulerepository.company;

import com.welcommu.moduledomain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    Optional<Company> findByBusinessNumber(String businessNumber);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByPhone(String phone);

}