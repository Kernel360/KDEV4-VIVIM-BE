package com.welcommu.moduleservice.company;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.dto.CompanyRequest;
import com.welcommu.moduledomain.company.dto.CompanyResponse;
import com.welcommu.moduleservice.exception.ResourceNotFoundException;
import com.welcommu.modulerepository.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyManagementService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyManagementService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // 회사 생성
    public CompanyResponse createCompany(CompanyRequest companyRequest) {
        // DTO를 Entity로 변환하여 저장
        Company company = new Company();
        company.setName(companyRequest.getName());
        company.setAddress(companyRequest.getAddress());
        company.setPhoneNumber(companyRequest.getPhoneNumber());
        company.setEmail(companyRequest.getEmail());

        // 회사 저장
        Company savedCompany = companyRepository.save(company);

        // 저장된 회사 정보를 DTO로 변환하여 반환
        return new CompanyResponse(savedCompany.getId(), savedCompany.getName(), savedCompany.getAddress(), savedCompany.getPhoneNumber(), savedCompany.getEmail());
    }

    // 전체 회사 조회
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // ID로 회사 조회
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // 회사 수정
    public Company updateCompany(Long id, Company updatedCompany) {
        // 기존 회사 조회
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        // 회사 삭제
        companyRepository.delete(existingCompany);
    }
}
