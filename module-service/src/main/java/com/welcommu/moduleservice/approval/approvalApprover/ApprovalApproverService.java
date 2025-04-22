package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void registerApprovers(Long approvalId, ApproverRegisterRequest request) {
        ApprovalProposal proposal = findProposal(approvalId);
        List<ProjectUser> allProjectUser = projectUserRepository.findAllById(
            request.getProjectUserIdList());
        List<ApprovalApprover> approvers = request.toAllApprover(proposal, allProjectUser);

        approvalApproverRepository.saveAll(approvers);
        proposal.setCountTotalApprover(approvers.size());
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

    private ApprovalProposal findProposal(Long approvalId) {
        return approvalProposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }
}
