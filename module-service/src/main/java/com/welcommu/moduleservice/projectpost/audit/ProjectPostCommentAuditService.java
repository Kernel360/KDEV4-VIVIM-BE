package com.welcommu.moduleservice.projectpost.audit;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.AuditLogFactory;
import com.welcommu.moduleservice.logging.AuditLogFieldComparator;
import com.welcommu.moduleservice.logging.AuditableService;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentSnapshot;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostSnapshot;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectPostCommentAuditService implements AuditableService<ProjectPostCommentSnapshot> {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;
    private final AuditLogFactory auditLogFactory;

    @Override
    public void createAuditLog(ProjectPostCommentSnapshot post, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.COMMENT, post.getId(), ActionType.CREATE, userId);
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(ProjectPostCommentSnapshot before, ProjectPostCommentSnapshot after, Long userId) {
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);
        AuditLog log = auditLogFactory.createWithFieldChanges(TargetType.COMMENT, after.getId(), ActionType.MODIFY, userId, changedFields);
        auditLogRepository.save(log);
    }

    @Override
    public void deleteAuditLog(ProjectPostCommentSnapshot post, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.COMMENT, post.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }

}
