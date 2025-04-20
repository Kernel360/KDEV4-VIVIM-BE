package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.modulerepository.approval.ApproverRepository;
import com.welcommu.modulerepository.approval.ProposalRepository;
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
public class ApproverService {

    private final ApproverRepository approverRepository;
    private final ProposalRepository proposalRepository;
    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void registerApprovers(Long approvalId, ApproverRegisterRequest request) {
        ApprovalProposal proposal = findProposal(approvalId);
        List<ProjectUser> allProjectUser = projectUserRepository.findAllById(
            request.getProjectUserIdList());
        List<ApprovalApprover> approvers = request.toAllApprover(proposal, allProjectUser);

        approverRepository.saveAll(approvers);
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
        approverRepository.deleteByApprovalProposal(proposal);
        proposal.setCountTotalApprover(0);
    }

    public List<ApproverResponse> getAllApprover(Long approvalId) {

        ApprovalProposal proposal = proposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));

        return approverRepository.findByApprovalProposal(proposal).stream()
            .map(ApproverResponse::of)
            .collect(Collectors.toList());
    }

    private ApprovalProposal findProposal(Long approvalId) {
        return proposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }
}
