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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyManagementService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    // 회사 생성
    public CompanyResponse createCompany(CompanyRequest companyRequest) {
        // CompanyRequest를 Entity로 변환
        Company company = companyRequest.toEntity();  // toEntity() 사용

        // 회사 저장
        Company savedCompany = companyRepository.save(company);

        // Builder 패턴을 사용하여 CompanyResponse 객체 생성
        return CompanyResponse.builder()
                .id(savedCompany.getId())
                .name(savedCompany.getName())
                .address(savedCompany.getAddress())
                .phone(savedCompany.getPhone())
                .email(savedCompany.getEmail())
                .coOwner(savedCompany.getCoOwner())
                .build();
    }



    // 전체 회사 조회
    public List<Company> getAllCompany() {
        List<Company> companies = companyRepository.findAll();
        // 디버깅을 위한 로그
        System.out.println("조회된 회사 수: " + companies.size());
        return companies;
    }

    // ID로 회사 조회
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // 회사별 직원 목록 조회
    public List<UserResponse> getEmployeesByCompany(Long companyId) {
        // 회사 ID에 해당하는 직원 목록 조회
        List<User> employees = userRepository.findByCompanyId(companyId);

        // Employee 엔티티를 UserResponse DTO로 변환하여 반환
        return employees.stream()
                .map(employee -> new UserResponse(employee.getId(), employee.getEmail(), employee.getName()))
                .collect(Collectors.toList());
    }

    // 회사 수정
    public Company updateCompany(Long id, Company updatedCompany) {
        // 기존 회사 조회
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        // 수정된 회사 정보로 업데이트
        existingCompany.setName(updatedCompany.getName());
        existingCompany.setAddress(updatedCompany.getAddress());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setCoOwner(updatedCompany.getCoOwner());
        // 회사 저장
        return companyRepository.save(existingCompany);
    }

    // 회사 삭제
    public void deleteCompany(Long id) {
        // 기존 회사 조회
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        // 회사 삭제
        companyRepository.delete(existingCompany);
    }
}
