package com.welcommu.moduleservice.company;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.modulerepository.company.CompanyRepository;
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
public class CompanyManagementService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyResponse createCompany(CompanyRequest companyRequest) {
        Company company = companyRequest.toEntity();
        Company savedCompany = companyRepository.save(company);

        return CompanyResponse.builder()
                .id(savedCompany.getId())
                .name(savedCompany.getName())
                .address(savedCompany.getAddress())
                .phone(savedCompany.getPhone())
                .email(savedCompany.getEmail())
                .coOwner(savedCompany.getCoOwner())
                .build();
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

    public Company updateCompany(Long id, Company updatedCompany) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        existingCompany.setName(updatedCompany.getName());
        existingCompany.setAddress(updatedCompany.getAddress());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setCoOwner(updatedCompany.getCoOwner());
        return companyRepository.save(existingCompany);
    }

    public void deleteCompany(Long id) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        companyRepository.delete(existingCompany);
    }
}
