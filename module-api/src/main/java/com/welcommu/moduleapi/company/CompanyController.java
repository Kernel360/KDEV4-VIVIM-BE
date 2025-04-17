package com.welcommu.moduleapi.company;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.CompanyService;
import com.welcommu.moduleservice.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "회사 API", description = "회사를 생성, 수정, 삭제시킬 수 있습니다.")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @Operation(summary = "회사를 생성합니다.")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyRequest companyRequest,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        log.info("Received company: {}", companyRequest);

        Long actorId = userDetails.getUser().getId();
        companyService.createCompany(companyRequest, actorId);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(),
            "회사가 성공적으로 생성되었습니다."
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "전체 회사 목록을 조회합니다.")
    public List<CompanyResponse> getCompanyList() {
        log.info("전체 회사 조회 API 호출됨.");
        List<Company> companies = companyService.getAllCompany();
        return companies.stream()
            .map(CompanyResponse::from)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "회사 id를 바탕으로 회사를 개별 조회합니다.")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        return company.map(c -> ResponseEntity.ok(CompanyResponse.from(c)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "id를 바탕으로 해당 회사에 소속된 유저를 전체 조회합니다.")
    @GetMapping("/{companyId}/employees")
    public ResponseEntity<ApiResponse> getEmployeesByCompany(@PathVariable Long companyId) {
        log.info("회사 ID {}에 속한 직원 목록 조회 API 호출됨.", companyId);

        List<UserResponse> employeeList = companyService.getEmployeesByCompany(companyId);

        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new ApiResponse(404, "해당 회사에 직원이 존재하지 않습니다."));
        }

        return ResponseEntity.ok(
            new ApiResponse(200, "해당 회사의 직원을 성공적으로 조회하였습니다.", employeeList));
    }

    @PutMapping("/{id}")
    @Operation(summary = "id를 바탕으로 회사를 수정합니다.")
    public ResponseEntity<ApiResponse> modifyCompany(@PathVariable Long id, @RequestBody Company modifiedCompany,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        companyService.modifyCompany(id, modifiedCompany,actorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "id를 바탕으로 회사를 삭제합니다.(Hard Delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCompany(@PathVariable Long id,@AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        companyService.deleteCompany(id, actorId);
        return ResponseEntity.noContent().build();
    }
}
