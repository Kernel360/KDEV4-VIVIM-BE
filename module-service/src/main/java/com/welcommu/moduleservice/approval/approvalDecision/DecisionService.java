package com.welcommu.moduleservice.approval.approvalDecision;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApproverRepository;
import com.welcommu.modulerepository.approval.DecisionRepository;
import com.welcommu.modulerepository.approval.ProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionCreateRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionModifyRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DecisionService {

    private final ProposalRepository proposalRepository;
    private final DecisionRepository decisionRepository;
    private final ApproverRepository approverRepository;

    @Transactional
    public void createDecision(User user, Long proposalId, DecisionCreateRequest request) {
        ApprovalProposal proposal = findProposal(proposalId);
        checkUserPermission(user, proposal);

        ApprovalApprover approver = findApprover(user, proposal);
        ApprovalDecision decision = request.toEntity(approver);
        decisionRepository.save(decision);

        // 상태 자동 갱신 로직
        proposal.modifyStatus(proposal);
    }


    @Transactional
    public void modifyDecision(Long decisionId, DecisionModifyRequest dto) {
        ApprovalDecision decision = findDecision(decisionId);

        if (dto.getTitle() != null) decision.setTitle(dto.getTitle());
        if (dto.getContent() != null) decision.setContent(dto.getContent());
        if (dto.getDecisionStatus() != null) decision.setDecisionStatus(dto.getDecisionStatus());

        ApprovalProposal proposal = decision.getApprovalApprover().getApprovalProposal();
        List<ApprovalDecision> decisions = decisionRepository.findByApprovalProposal(proposal);
        proposal.modifyStatus(decisions, proposal.getCountTotalApprover());
    }

    @Transactional
    public void deleteDecision(Long decisionId) {
        ApprovalDecision decision = findDecision(decisionId);
        decisionRepository.delete(decision);
    }

    @Transactional
    public DecisionResponse getDecision(Long decisionId) {
        ApprovalDecision decision = findDecision(decisionId);
        return DecisionResponse.from(decision);
    }

    @Transactional
    public List<DecisionResponse> getDecisionListByProposal(Long proposalId) {
        ApprovalProposal proposal = findProposal(proposalId);
        return decisionRepository.findByApprovalProposal(proposal)
                .stream()
                .map(DecisionResponse::from)
                .collect(Collectors.toList());
    }

    private void checkUserPermission(User user, ApprovalProposal proposal) {
        if (isAdmin(user)) return;

        if (isCustomer(user)) {
            boolean isApprover = approverRepository
                    .findByApprovalProposalAndProjectUserUser(proposal, user)
                    .isPresent();

            if (!isApprover) {
                throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_APPROVER);
            }
            return;
        }

        throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_CUSTOMER);
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().toString().equals("ADMIN");
    }

    private boolean isCustomer(User user) {
        return user.getRole() != null && user.getRole().toString().equals("CUSTOMER");
    }

    private ApprovalProposal findProposal(Long proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private ApprovalApprover findApprover(User user, ApprovalProposal proposal) {

        return approverRepository.findByApprovalProposalAndProjectUserUser(proposal, user)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_APPROVER));
    }

    private ApprovalDecision findDecision(Long decisionId) {
        return decisionRepository.findById(decisionId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_DECISION));
    }
}
