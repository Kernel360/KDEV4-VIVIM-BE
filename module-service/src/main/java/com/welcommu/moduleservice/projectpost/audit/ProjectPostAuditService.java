package com.welcommu.moduleservice.projectpost.audit;

import com.welcommu.moduleservice.projectpost.dto.ProjectPostSnapshot;

public interface ProjectPostAuditService {
    void createAuditLog(ProjectPostSnapshot post, Long userId);
    void modifyAuditLog(ProjectPostSnapshot before, ProjectPostSnapshot after, Long userId);
    void deleteAuditLog(ProjectPostSnapshot post, Long userId);
}
