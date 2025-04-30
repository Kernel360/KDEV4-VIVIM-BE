package com.welcommu.moduleservice.company.audit;

import com.welcommu.moduleservice.company.dto.CompanySnapshot;

public interface CompanyAuditService {
    void createAuditLog(CompanySnapshot company, Long userId);
    void modifyAuditLog(CompanySnapshot before, CompanySnapshot after, Long userId);
    void deleteAuditLog(CompanySnapshot company, Long userId);
}
