package com.welcommu.moduleservice.approval.approvalDecision;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalDecisionRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionRequestCreation;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionRequestModification;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponseSend;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DecisionService {

    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ApprovalApproverRepository approvalApproverRepository;

    @Transactional
    public void createDecision(User user, Long proposalId, Long approverId,
        DecisionRequestCreation request) {

        ApprovalProposal proposal = findProposal(proposalId);
        ApprovalApprover approver = findApprover(approverId);
        checkUserPermission(user, proposal);

        ApprovalDecision decision = request.toEntity(approver);
        approvalDecisionRepository.save(decision);
    }

    @Transactional
    public void modifyDecision(Long decisionId, DecisionRequestModification dto) {

        ApprovalDecision decision = findDecision(decisionId);
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

    //    public DecisionResponseByFilteredApproverList getDecision(Long proposalId) {
    //        ApprovalProposal proposal = approvalProposalRepository.findById(proposalId)
    //            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    //        return DecisionResponseByFilteredApproverList.getFilteredDecisionResponses(
    //            List.of(proposal), approvalApproverRepository, approvalDecisionRepository);
    //    }

    @Transactional
    public DecisionResponseSend sendDecision(User user, Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        checkUserPermission(user, decision.getApprovalApprover().getApprovalProposal());

        // 승인응답 전송할 때를 기준으로 응답시간 기록
        decision.setDecidedAt(LocalDateTime.now());

        // 승인요청 상태 변경
        ApprovalProposal proposal = decision.getApprovalApprover().getApprovalProposal();
        List<ApprovalDecision> allDecisions = approvalDecisionRepository.findByApprovalApprover_ApprovalProposal(
            proposal);
        proposal.modifyProposalStatus(allDecisions, proposal.getCountTotalApprover());

        return DecisionResponseSend.from(user, decision);
    }

    private void checkUserPermission(User user, ApprovalProposal proposal) {

        // 관리자일 경우 API 사용허가
        if (isAdmin(user)) {
            return;
        }

        // CompanyRole 이 고객사면서 승인권자인 경우 API 사용허가
        if (isCustomer(user)) {
            boolean isApprover = approvalApproverRepository.findByApprovalProposalAndProjectUserUser(
                proposal, user).isPresent();

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

    private ApprovalApprover findApprover(Long approverId) {
        return approvalApproverRepository.findById(approverId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_APPROVER));
    }

    private ApprovalDecision findDecision(Long decisionId) {
        return approvalDecisionRepository.findById(decisionId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_DECISION));
    }
}
