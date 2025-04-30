package com.welcommu.moduleservice.projectpost.audit;

import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentSnapshot;

public interface ProjectPostCommentAuditService {
    void createAuditLog(ProjectPostCommentSnapshot post, Long userId);
    void modifyAuditLog(ProjectPostCommentSnapshot before, ProjectPostCommentSnapshot after, Long userId);
    void deleteAuditLog(ProjectPostCommentSnapshot post, Long userId);
}
