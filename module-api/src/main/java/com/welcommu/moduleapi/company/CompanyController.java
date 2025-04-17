package com.welcommu.moduleapi.company;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.CompanyService;
import com.welcommu.moduleservice.logging.CompanyAuditLog;
import com.welcommu.moduleservice.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "회사 API", description = "회사를 생성, 수정, 삭제시킬 수 있습니다.")
public class CompanyController {

    private final CompanyService companyManagementService;
    private final CompanyAuditLog companyAuditLog;


    @PostMapping
    @Operation(summary = "회사를 생성합니다.")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyRequest companyRequest,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        log.info("Received company: {}", companyRequest);

        Long actorId = userDetails.getUser().getId();
        companyManagementService.createCompany(companyRequest, actorId);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), "Company created successfully"
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "전체 회사 목록을 조회합니다.")
    public List<CompanyResponse> getCompanyList() {
        log.info("전체 회사 조회 API 호출됨.");
        List<Company> companies = companyManagementService.getAllCompany();
        return companies.stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "회사 id를 바탕으로 회사를 개별 조회합니다.")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyManagementService.getCompanyById(id);
        return company.map(c -> ResponseEntity.ok(CompanyResponse.from(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "id를 바탕으로 해당 회사에 소속된 유저를 전체 조회합니다.")
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
    @Operation(summary = "id를 바탕으로 회사를 수정합니다.")
    public ResponseEntity<Company> modifyCompany(@PathVariable Long id, @RequestBody Company modifiedCompany,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        try {
            Long actorId = userDetails.getUser().getId();
            Company company = companyManagementService.modifyCompany(id, modifiedCompany,actorId);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "id를 바탕으로 회사를 삭제합니다.(Hard Delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        companyManagementService.deleteCompany(id, actorId);
        return ResponseEntity.noContent().build();
    }
}
