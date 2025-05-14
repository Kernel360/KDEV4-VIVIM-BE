package com.welcommu.moduleservice.company;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.user.UserRepository;
import com.welcommu.moduleservice.company.audit.CompanyAuditService;
import com.welcommu.moduleservice.company.dto.CompanyModifyRequest;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleinfra.company.CompanyRepository;
import com.welcommu.moduleservice.company.dto.CompanyResponse;
import com.welcommu.moduleservice.company.dto.CompanySnapshot;
import com.welcommu.moduleservice.user.dto.UserResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyAuditService companyAuditLog;

    public void createCompany(CompanyRequest companyRequest, Long userId) {
        Company company = companyRequest.toEntity();
        Company savedCompany = companyRepository.save(company);

        companyAuditLog.createAuditLog(CompanySnapshot.from(savedCompany), userId);
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

    @Transactional
    public Company modifyCompany(Long id, CompanyModifyRequest request, Long modifierId) {
        // 1) DB에서 현재 엔티티 조회
        Company existing = companyRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));

        // 2) “클라이언트가 본 버전” vs “DB 실제 버전” 비교
        if (!existing.getVersion().equals(request.getVersion())) {
            // UI에서 본 데이터가 DB에서 이미 갱신된 상태니까 충돌 처리
            throw new CustomException(CustomErrorCode.CONCURRENT_UPDATE);
        }

        // 3) 변경 전 스냅샷
        CompanySnapshot before = CompanySnapshot.from(existing);

        // 4) 실제 필드 수정
        request.modifyCompany(existing);

        // 5) 저장(이때 JPA가 버전을 +1 자동증가)
        Company saved = companyRepository.saveAndFlush(existing);

        // 6) 변경 후 스냅샷 및 감사로그
        CompanySnapshot after = CompanySnapshot.from(saved);
        companyAuditLog.modifyAuditLog(before, after, modifierId);

        return saved;
    }

    public void deleteCompany(Long id, Long deleterId) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        companyAuditLog.deleteAuditLog(CompanySnapshot.from(existingCompany) , deleterId);
        companyRepository.delete(existingCompany);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponse> searchCompanies(String name, String businessNumber, String email, Boolean isDeleted, Pageable pageable) {
        return companyRepository.searchByConditions(name, businessNumber, email, isDeleted, pageable)
            .map(CompanyResponse::from);
    }

    @Transactional
    @Override
    public void softDeleteCompany(Long id, Long deleterId) {
        Company existingCompany = companyRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
        existingCompany.setIsDeleted(true);
        existingCompany.setDeletedAt(LocalDateTime.now());
        companyAuditLog.deleteAuditLog(CompanySnapshot.from(existingCompany) , deleterId);
    }
}
