package com.welcommu.moduleservice.approval.approvalDecision;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.approval.ApprovalApproverRepository;
import com.welcommu.moduleinfra.approval.ApprovalDecisionRepository;
import com.welcommu.moduleinfra.approval.ApprovalProposalRepository;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionCreateRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionModifyRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponse;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionSendResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApprovalDecisionService {

    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ApprovalApproverRepository approvalApproverRepository;

    @Transactional
    public void createDecision(User user, Long proposalId, DecisionCreateRequest request) {

        ApprovalProposal proposal = findProposal(proposalId);
        checkUserPermission(user, proposal);

        ApprovalApprover approver = findApprover(user, proposal);
        ApprovalDecision decision = request.toEntity(approver);
        approvalDecisionRepository.save(decision);
    }


    @Transactional
    public void modifyDecision(Long decisionId, DecisionModifyRequest dto) {

        ApprovalDecision decision = findDecision(decisionId);

        if (dto.getTitle() != null) {
            decision.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            decision.setContent(dto.getContent());
        }
        if (dto.getDecisionStatus() != null) {
            decision.setDecisionStatus(dto.getDecisionStatus());
        }
    }

    @Transactional
    public void deleteDecision(Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        approvalDecisionRepository.delete(decision);
    }

    public DecisionResponse getDecision(Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        return DecisionResponse.from(decision);
    }

    public List<DecisionResponse> getAllDecision(Long proposalId) {

        ApprovalProposal proposal = findProposal(proposalId);
        return approvalDecisionRepository.findByApprovalApprover_ApprovalProposal(proposal)
            .stream()
            .map(DecisionResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public DecisionSendResponse sendDecision(User user, Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        checkUserPermission(user, decision.getApprovalApprover().getApprovalProposal());

        // 승인응답 전송할 때를 기준으로 응답시간 기록
        decision.setDecidedAt(LocalDateTime.now());

        // 승인요청 상태 변경
        ApprovalProposal proposal = decision.getApprovalApprover().getApprovalProposal();
        List<ApprovalDecision> allDecisions = approvalDecisionRepository.findByApprovalApprover_ApprovalProposal(
            proposal);
        proposal.modifyProposalStatus(allDecisions, proposal.getCountTotalApprover());

        return DecisionSendResponse.from(user, decision);
    }

    private void checkUserPermission(User user, ApprovalProposal proposal) {

        // 관리자일 경우 API 사용허가
        if (isAdmin(user)) {
            return;
        }

        // CompanyRole 이 고객사면서 승인권자인 경우 API 사용허가
        if (isCustomer(user)) {
            boolean isApprover = approvalApproverRepository
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
        return approvalProposalRepository.findById(proposalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private ApprovalApprover findApprover(User user, ApprovalProposal proposal) {

        return approvalApproverRepository.findByApprovalProposalAndProjectUserUser(proposal, user)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_APPROVER));
    }

    private ApprovalDecision findDecision(Long decisionId) {
        return approvalDecisionRepository.findById(decisionId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_DECISION));
    }
}
