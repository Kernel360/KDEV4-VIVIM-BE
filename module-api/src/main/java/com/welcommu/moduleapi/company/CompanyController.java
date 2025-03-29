package com.welcommu.moduleapi.company;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.CompanyManagementService;
import com.welcommu.moduleservice.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyManagementService companyManagementService;
  
    // 회사 등록
    @PostMapping
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
        log.info("Received company: {}", companyRequest);

        // 서비스에서 회사 등록 처리 후 응답 받기
        CompanyResponse createdCompanyResponse = companyManagementService.createCompany(companyRequest);

        // ApiResponse로 상태 코드와 메시지만 포함하여 반환
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.CREATED.value(),   // 상태 코드 (201)
                "Company created successfully"  // 상태 메시지
        );

        // 생성된 회사 정보와 함께 HTTP 201 응답 반환
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // 전체 회사 조회
    @GetMapping
    public List<Company> getCompanyList() {
        log.info("전체 회사 조회 API 호출됨.");
        return companyManagementService.getAllCompany();
    }

    // ID로 회사 조회
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyManagementService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // 회사별 직원 목록 조회
    @GetMapping("/{companyId}/employees")
    public ResponseEntity<ApiResponse> getEmployeesByCompany(@PathVariable Long companyId) {
        log.info("회사 ID {}에 속한 직원 목록 조회 API 호출됨.", companyId);

        List<UserResponse> employeeList = companyManagementService.getEmployeesByCompany(companyId);

        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "No employees found for the company"));
        }

        return ResponseEntity.ok(new ApiResponse(200, "Employees retrieved successfully", employeeList));
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
