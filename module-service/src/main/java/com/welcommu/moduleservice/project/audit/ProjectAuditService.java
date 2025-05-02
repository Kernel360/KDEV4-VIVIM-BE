package com.welcommu.moduleservice.project.audit;

import com.welcommu.moduleservice.project.dto.ProjectSnapshot;

public interface ProjectAuditService {
    void createAuditLog(ProjectSnapshot project, Long userId);
    void modifyAuditLog(ProjectSnapshot before, ProjectSnapshot after, Long userId);
    void deleteAuditLog(ProjectSnapshot project, Long userId);
}
