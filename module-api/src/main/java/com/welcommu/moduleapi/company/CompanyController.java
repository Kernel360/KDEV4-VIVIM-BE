package com.welcommu.moduleapi.company;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.dto.CompanyRequest;
import com.welcommu.moduledomain.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.CompanyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyManagementService companyManagementService;

    @Autowired
    public CompanyController(CompanyManagementService companyManagementService) {
        this.companyManagementService = companyManagementService;
    }

    // 회사 등록
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
        System.out.println("Received company: " + companyRequest); // 요청 받은 데이터 출력

        // 서비스에서 회사 등록 처리 후 응답 받기
        CompanyResponse createdCompanyResponse = companyManagementService.createCompany(companyRequest);

        // 생성된 회사 정보와 함께 HTTP 201 응답 반환
        return new ResponseEntity<>(createdCompanyResponse, HttpStatus.CREATED);
    }

    // 전체 회사 조회
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyManagementService.getAllCompanies();
    }

    // ID로 회사 조회
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyManagementService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 회사 수정
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        try {
            Company company = companyManagementService.updateCompany(id, updatedCompany);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 회사 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyManagementService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
