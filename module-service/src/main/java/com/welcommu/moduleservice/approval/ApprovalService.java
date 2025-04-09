package com.welcommu.moduleservice.approval;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalRepository;
import com.welcommu.modulerepository.checklist.ChecklistRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.moduleservice.approval.dto.ApprovalRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ChecklistRepository checklistRepository;
    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void createApproval(User user, Long checklistId, ApprovalRequest request) {
        Checklist checklist = findChecklist(checklistId);
        Long projectId = checklist.getProjectProgress().getProject().getId();

        // 관리자일 경우 바로 승인 생성 허용
        if ("ADMIN".equals(user.getRole().toString())) {
            saveApproval(checklist, request);
            return;
        }

        // 관리자 외 사용자만 projectUser 확인
        ProjectUser projectUser = findProjectUser(user.getId(), projectId);
        ProjectUserManageRole role = projectUser.getProjectUserManageRole();

        // 역할에 따라 분기 처리
        switch (role) {
            case DEVELOPER_MANAGER -> validateAndHandle(user, projectUser, checklist, request, ProjectUserManageRole.DEVELOPER_MANAGER);
            case CLIENT_MANAGER -> validateAndHandle(user, projectUser, checklist, request, ProjectUserManageRole.CLIENT_MANAGER);
            default -> throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private void validateAndHandle(User user, ProjectUser projectUser, Checklist checklist, ApprovalRequest request, ProjectUserManageRole requiredRole) {
        checkUserPermission(user, projectUser, requiredRole);
        saveApproval(checklist, request);
    }

    private void saveApproval(Checklist checklist, ApprovalRequest request) {
        Approval approval = request.toEntity(checklist);
        approvalRepository.save(approval);
    }

    private void checkUserPermission(User user, ProjectUser projectUser, ProjectUserManageRole requiredRole) {
        if (user.getCompany() == null || !Objects.equals(projectUser.getProjectUserManageRole(), requiredRole)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private ProjectUser findProjectUser(Long userId, Long projectId) {
        return projectUserRepository
            .findByUserIdAndProjectId(userId, projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }

    private Checklist findChecklist(Long checklistId) {
        return checklistRepository.findById(checklistId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CHECKLIST));
    }
}
