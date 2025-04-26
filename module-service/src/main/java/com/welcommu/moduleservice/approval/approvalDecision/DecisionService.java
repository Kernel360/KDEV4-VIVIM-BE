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
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponsesByAllApprover;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponsesByOneApprover;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DecisionService {

    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ApprovalApproverRepository approvalApproverRepository;

    @Transactional
    public void createDecision(User user, Long approverId, DecisionRequestCreation request) {

        ApprovalApprover approver = findApprover(approverId);
        ApprovalProposal proposal = approver.getApprovalProposal();
        checkUserPermission(user, proposal);

        ApprovalDecision decision = request.toEntity(approver);
        approvalDecisionRepository.save(decision);

        // 상태 변경 후 proposal 상태도 갱신
        List<ApprovalDecision> decisions = findAllDecision(approverId);
        proposal.modifyProposalStatus(decisions, proposal.getCountTotalApprover());
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

        ApprovalProposal proposal = decision.getApprovalApprover()
            .getApprovalProposal();

        List<ApprovalDecision> decisions = findAllDecision(decision.getApprovalApprover()
            .getId());
        proposal.modifyProposalStatus(decisions, proposal.getCountTotalApprover());
    }

    @Transactional
    public void deleteDecision(Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        approvalDecisionRepository.delete(decision);
    }

    @Transactional
    public DecisionResponseSend sendDecision(User user, Long decisionId) {

        ApprovalDecision decision = findDecision(decisionId);
        checkUserPermission(user, decision.getApprovalApprover()
            .getApprovalProposal());

        // 승인응답 전송할 때를 기준으로 응답시간 기록
        decision.setDecidedAt(LocalDateTime.now());

        // 승인요청 상태 변경
        ApprovalProposal proposal = decision.getApprovalApprover()
            .getApprovalProposal();
        List<ApprovalDecision> allDecisions = approvalDecisionRepository.findByApprovalApprover_ApprovalProposal(proposal);
        proposal.modifyProposalStatus(allDecisions, proposal.getCountTotalApprover());

        return DecisionResponseSend.from(user, decision);
    }

    public DecisionResponsesByAllApprover getFilteredAllDecisions(Long approvalId) {

        ApprovalProposal approvalProposal = findProposal(approvalId);
        List<ApprovalApprover> approvers = findApproverByProposal(approvalProposal);

        List<DecisionResponsesByOneApprover> decisionResponses = approvers.stream()
            .map(approver -> {
                List<ApprovalDecision> decisions = approvalDecisionRepository.findByApprovalApproverId(approver.getId());

                return DecisionResponsesByOneApprover.from(approver, decisions);
            })
            .collect(Collectors.toList());

        return DecisionResponsesByAllApprover.from(approvalProposal.getId(), approvalProposal.getTitle(), decisionResponses);
    }

    private void checkUserPermission(User user, ApprovalProposal proposal) {

        // 관리자일 경우 API 사용허가
        if (isAdmin(user)) {
            return;
        }

        // CompanyRole 이 고객사면서 승인권자인 경우 API 사용허가
        if (isCustomer(user)) {
            boolean isApprover = approvalApproverRepository.findByApprovalProposalAndProjectUserUser(proposal, user)
                .isPresent();

            if (!isApprover) {
                throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_APPROVER);
            }
            return;
        }

        throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_CUSTOMER);
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole()
            .toString()
            .equals("ADMIN");
    }

    private boolean isCustomer(User user) {
        return user.getRole() != null && user.getRole()
            .toString()
            .equals("CUSTOMER");
    }

    private ApprovalProposal findProposal(Long proposalId) {
        return approvalProposalRepository.findById(proposalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private ApprovalApprover findApprover(Long approverId) {
        return approvalApproverRepository.findById(approverId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_APPROVER));
    }

    private List<ApprovalApprover> findApproverByProposal(ApprovalProposal proposal) {
        return approvalApproverRepository.findByApprovalProposal(proposal);
    }

    private ApprovalDecision findDecision(Long decisionId) {
        return approvalDecisionRepository.findById(decisionId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_DECISION));
    }

    private List<ApprovalDecision> findAllDecision(Long approverId) {
        return approvalDecisionRepository.findAllByApprovalApproverId(approverId);
    }
}
