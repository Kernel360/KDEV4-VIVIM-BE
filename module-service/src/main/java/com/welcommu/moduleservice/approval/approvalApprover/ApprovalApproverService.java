package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverRegisterRequest;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalApproverService {

    private final ApprovalApproverRepository approvalApproverRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void registerApprovers(Long approvalId, ApproverRegisterRequest request) {
        ApprovalProposal proposal = findProposal(approvalId);
        Long projectId = proposal.getProjectProgress().getProject().getId();

        for (Long userId : request.getApproverUserIds()) {
            ProjectUser projectUser = findProjectUser(userId, projectId);
            checkClientCompanyRole(projectUser);

            // 중복 승인권자 방지
            if (approvalApproverRepository.existsByApprovalProposalAndProjectUser(proposal,
                projectUser)) {
                throw new CustomException(CustomErrorCode.DUPLICATED_APPROVAL_APPROVER);
            }

            approvalApproverRepository.save(
                ApprovalApprover.builder()
                    .approvalProposal(proposal)
                    .projectUser(projectUser)
                    .build()
            );
        }

        proposal.setCountTotalApprover(request.getApproverUserIds().size());
    }


    @Transactional
    public void modifyApprovers(Long approvalId, ApproverRegisterRequest request) {
        deleteAllApprovers(approvalId);
        registerApprovers(approvalId, request);
    }

    @Transactional
    public void deleteAllApprovers(Long approvalId) {
        ApprovalProposal proposal = findProposal(approvalId);
        approvalApproverRepository.deleteByApprovalProposal(proposal);
        proposal.setCountTotalApprover(0);
    }

    public List<ApproverResponse> getAllApprover(Long approvalId) {

        ApprovalProposal proposal = approvalProposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));

        return approvalApproverRepository.findByApprovalProposal(proposal).stream()
            .map(ApproverResponse::from)
            .collect(Collectors.toList());
    }

    private void checkClientCompanyRole(ProjectUser projectUser) {
        ProjectUserManageRole role = projectUser.getProjectUserManageRole();

        if (role != ProjectUserManageRole.CLIENT_USER
            && role != ProjectUserManageRole.CLIENT_MANAGER) {
            throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private ApprovalProposal findProposal(Long approvalId) {
        return approvalProposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private ProjectUser findProjectUser(Long userId, Long projectId) {
        return projectUserRepository.findByUserIdAndProjectId(userId, projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }
}
