package com.welcommu.moduleapi.company;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.CompanyManagementService;
import com.welcommu.moduleservice.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyManagementService companyManagementService;

    @PostMapping
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
        log.info("Received company: {}", companyRequest);

        CompanyResponse createdCompanyResponse = companyManagementService.createCompany(companyRequest);

        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.CREATED.value(),
                "Company created successfully"
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CompanyResponse> getCompanyList() {
        log.info("전체 회사 조회 API 호출됨.");
        List<Company> companies = companyManagementService.getAllCompany();
        return companies.stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyManagementService.getCompanyById(id);
        return company.map(c -> ResponseEntity.ok(CompanyResponse.from(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{companyId}/employees")
    public ResponseEntity<ApiResponse> getEmployeesByCompany(@PathVariable Long companyId) {
        log.info("회사 ID {}에 속한 직원 목록 조회 API 호출됨.", companyId);

        List<UserResponse> employeeList = companyManagementService.getEmployeesByCompany(companyId);

        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "No employees found for the company"));
        }

        return ResponseEntity.ok(new ApiResponse(200, "Employees retrieved successfully", employeeList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        try {
            Company company = companyManagementService.updateCompany(id, updatedCompany);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyManagementService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
