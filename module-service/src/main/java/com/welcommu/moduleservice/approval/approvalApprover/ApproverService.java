package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalApproverStatus;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalDecisionRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverRequestCreate;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponseList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApproverService {

    private final ApprovalApproverRepository approvalApproverRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void createApprover(User user, Long proposalId, ApproverRequestCreate request) {
        ApprovalProposal proposal = findProposal(proposalId);
        Long projectId = proposal.getProjectProgress()
            .getProject()
            .getId();
        checkUserPermission(user, projectId);

        for (Long userId : request.getApproverIds()) {
            ProjectUser projectUser = findProjectUser(userId, projectId);
            checkClientCompanyRole(projectUser);

            // 중복 승인권자 방지
            if (approvalApproverRepository.existsByApprovalProposalAndProjectUser(proposal, projectUser)) {
                throw new CustomException(CustomErrorCode.DUPLICATED_APPROVAL_APPROVER);
            }

            ApprovalApprover approver = request.toEntity(proposal, projectUser);
            approvalApproverRepository.save(approver);
        }

        proposal.setCountTotalApprover(request.getApproverIds()
            .size());
    }

    @Transactional
    public void modifyApprovers(User user, Long proposalId, ApproverRequestCreate request) {

        ApprovalProposal proposal = findProposal(proposalId);
        Long projectId = proposal.getProjectProgress().getProject().getId();
        checkUserPermission(user, projectId);

        List<ApprovalApprover> existingApprovers = approvalApproverRepository.findByApprovalProposal(proposal);
        List<Long> existingUserIds = existingApprovers.stream()
            .map(approver -> approver.getProjectUser().getUser().getId())
            .toList();

        List<Long> requestedUserIds = request.getApproverIds();

        // 추가해야 할 승인권자 (요청에만 있고, 기존에는 없는 경우)
        List<Long> toAdd = requestedUserIds.stream()
            .filter(id -> !existingUserIds.contains(id))
            .toList();

        // 삭제해야 할 승인권자 (기존에는 있는데, 요청에는 없는 경우)
        List<ApprovalApprover> toDelete = existingApprovers.stream()
            .filter(approver -> !requestedUserIds.contains(approver.getProjectUser().getUser().getId()))
            .toList();

        // 1. 삭제
        approvalApproverRepository.deleteAll(toDelete);

        // 2. 추가
        for (Long userId : toAdd) {
            ProjectUser projectUser = findProjectUser(userId, projectId);
            checkClientCompanyRole(projectUser);

            ApprovalApprover approver = request.toEntity(proposal, projectUser);
            approvalApproverRepository.save(approver);
        }

        // 3. 총 승인권자 수 갱신
        int finalCount = approvalApproverRepository.findByApprovalProposal(proposal).size();
        proposal.setCountTotalApprover(finalCount);
    }

    @Transactional
    public void modifyApproverStatus(Long approverId) {
        ApprovalApprover approver = findApprover(approverId);
        List<ApprovalDecision> decisions = approvalDecisionRepository.findByApprovalApprover(approver);

        if (decisions.isEmpty()) {
            approver.setApproverStatus(ApprovalApproverStatus.NOT_RESPONDED);
            return;
        }

        boolean hasApproved = decisions.stream()
            .anyMatch(d -> d.getDecisionStatus() == ApprovalDecisionStatus.APPROVED);

        if (hasApproved) {
            approver.setApproverStatus(ApprovalApproverStatus.APPROVER_APPROVED);
            return;
        }

        boolean hasRejected = decisions.stream()
            .anyMatch(d -> d.getDecisionStatus() == ApprovalDecisionStatus.REJECTED);

        if (hasRejected) {
            approver.setApproverStatus(ApprovalApproverStatus.APPROVER_REJECTED);
        } else {
            approver.setApproverStatus(ApprovalApproverStatus.NOT_RESPONDED);
        }
    }

    @Transactional
    public void deleteAllApprovers(Long proposalId) {
        ApprovalProposal proposal = findProposal(proposalId);
        approvalApproverRepository.deleteByApprovalProposal(proposal);
        proposal.setCountTotalApprover(0);
    }

    public ApproverResponseList getAllApprovers(Long proposalId) {
        ApprovalProposal proposal = findProposal(proposalId);
        List<ApprovalApprover> approvalApprovers = approvalApproverRepository.findByApprovalProposal(proposal);

        return ApproverResponseList.from(approvalApprovers);
    }

    private void checkClientCompanyRole(ProjectUser projectUser) {
        ProjectUserManageRole role = projectUser.getProjectUserManageRole();

        if (role != ProjectUserManageRole.CLIENT_USER &&
            role != ProjectUserManageRole.CLIENT_MANAGER) {
            throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private void checkUserPermission(User user, Long projectId) {
        if (isAdmin(user)) {
            return;
        }
        if (isDeveloper(user)) {
            findProjectUser(user, projectId);
        } else {
            throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_DEVELOPER);
        }
    }

    private boolean isAdmin(User user) {
        return user.getRole()
            .toString()
            .equals("ADMIN");
    }

    private boolean isDeveloper(User user) {
        return user.getRole() != null && user.getRole()
            .name()
            .equals("DEVELOPER");
    }

    private void findProjectUser(User user, Long projectId) {
        projectUserRepository.findByUserIdAndProjectId(user.getId(), projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }

    private ApprovalProposal findProposal(Long proposalId) {
        return approvalProposalRepository.findById(proposalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private ApprovalApprover findApprover(Long approverId) {
        return approvalApproverRepository.findById(approverId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_APPROVER));
    }

    private ProjectUser findProjectUser(Long userId, Long projectId) {
        return projectUserRepository.findByUserIdAndProjectId(userId, projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }
}
