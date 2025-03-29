package com.welcommu.moduleservice.company;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.modulerepository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

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
                .phoneNumber(savedCompany.getPhoneNumber())
                .email(savedCompany.getEmail())
                .build();
    }



    // 전체 회사 조회
    public List<Company> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        // 디버깅을 위한 로그
        System.out.println("조회된 회사 수: " + companies.size());
        return companies;
    }

    // ID로 회사 조회
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // 회사 수정
    public Company updateCompany(Long id, Company updatedCompany) {
        // 기존 회사 조회
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        // 수정된 회사 정보로 업데이트
        existingCompany.setName(updatedCompany.getName());
        existingCompany.setAddress(updatedCompany.getAddress());
        existingCompany.setPhoneNumber(updatedCompany.getPhoneNumber());
        existingCompany.setEmail(updatedCompany.getEmail());

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
