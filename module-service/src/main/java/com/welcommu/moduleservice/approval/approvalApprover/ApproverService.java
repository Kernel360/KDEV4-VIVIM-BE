package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
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

        deleteAllApprovers(proposalId);
        createApprover(user, proposalId, request);
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

    private ProjectUser findProjectUser(Long userId, Long projectId) {
        return projectUserRepository.findByUserIdAndProjectId(userId, projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }
}
