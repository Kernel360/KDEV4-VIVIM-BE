package com.welcommu.moduleservice.company;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.moduleservice.logging.CompanyAuditLog;
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

        companyAuditLog.createAuditLog(savedCompany, userId);
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

    public Company modifyCompany(Long id, Company updatedCompany, Long modifierId) {
        Company existingCompany = companyRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        Company beforeCompany = Company.builder()
            .id(existingCompany.getId())
            .name(existingCompany.getName())
            .phone(existingCompany.getPhone())
            .email(existingCompany.getEmail())
            .address(existingCompany.getAddress())
            .coOwner(existingCompany.getCoOwner())
            .businessNumber(existingCompany.getBusinessNumber())
            .companyRole(existingCompany.getCompanyRole())
            .build();

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setAddress(updatedCompany.getAddress());
        existingCompany.setCoOwner(updatedCompany.getCoOwner());
        existingCompany.setBusinessNumber(updatedCompany.getBusinessNumber());
        existingCompany.setCompanyRole(updatedCompany.getCompanyRole());

        Company afterCompany =  companyRepository.save(existingCompany);

        companyAuditLog.modifyAuditLog(beforeCompany, afterCompany, modifierId);
        return afterCompany;
    }

    public void deleteCompany(Long id, Long deleterId) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        companyAuditLog.deleteAuditLog(existingCompany, deleterId);
        companyRepository.delete(existingCompany);
    }
}
