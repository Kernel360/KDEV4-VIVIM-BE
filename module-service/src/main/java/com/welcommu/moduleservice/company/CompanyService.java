package com.welcommu.moduleservice.company;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.company.dto.CompanyModifyRequest;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.moduleservice.logging.CompanyAuditLog;
import com.welcommu.moduleservice.logging.dto.CompanySnapshot;
import com.welcommu.moduleservice.logging.dto.UserSnapshot;
import com.welcommu.moduleservice.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyAuditLog companyAuditLog;

    public void createCompany(CompanyRequest companyRequest, Long userId) {
        Company company = companyRequest.toEntity();
        Company savedCompany = companyRepository.save(company);

        companyAuditLog.createAuditLog(CompanySnapshot.from(savedCompany), userId);
    }

    public List<Company> getAllCompany() {
        List<Company> companies = companyRepository.findAll();
        return companies;
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public List<UserResponse> getEmployeesByCompany(Long companyId) {
        List<User> employees = userRepository.findByCompanyId(companyId);
        return employees.stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }

    public Company modifyCompany(Long id, CompanyModifyRequest request, Long modifierId) {
        Company existingCompany = companyRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        CompanySnapshot beforeSnapshot = CompanySnapshot.from(existingCompany);
        request.modifyCompany(existingCompany);
        Company savedCompany =  companyRepository.save(existingCompany);
        CompanySnapshot afterSnapshot = CompanySnapshot.from(savedCompany);

        companyAuditLog.modifyAuditLog(beforeSnapshot, afterSnapshot, modifierId);

        return savedCompany;
    }

    public void deleteCompany(Long id, Long deleterId) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        companyAuditLog.deleteAuditLog(CompanySnapshot.from(existingCompany) , deleterId);
        companyRepository.delete(existingCompany);
    }
}
