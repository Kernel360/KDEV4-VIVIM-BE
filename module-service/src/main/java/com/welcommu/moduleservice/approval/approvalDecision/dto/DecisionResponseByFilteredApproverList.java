package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalDecisionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

/**
 * 여러 승인요청(ApprovalProposal)에 대한 승인권자 및 응답정보
 */

@Getter
@Builder
public class DecisionResponseByFilteredApproverList {
    
    private List<DecisionResponseByFilteredApprover> items;
    
    // 전체 승인요청(Proposal) 목록에 대해 승인권자와 그들의 응답 목록을 조회
    public static DecisionResponseByFilteredApproverList getFilteredDecisionResponses(
        List<ApprovalProposal> proposals, ApprovalApproverRepository approverRepo,
        ApprovalDecisionRepository decisionRepo) {
        List<DecisionResponseByFilteredApprover> items = proposals.stream()
            .map(proposal -> getDecisionResponsesForProposal(proposal, approverRepo, decisionRepo))
            .collect(Collectors.toList());
        
        return DecisionResponseByFilteredApproverList.builder().items(items).build();
    }
    
    // 하나의 승인요청(Proposal)에 대한 승인권자와 그들의 응답 목록을 조회
    private static DecisionResponseByFilteredApprover getDecisionResponsesForProposal(
        ApprovalProposal proposal, ApprovalApproverRepository approverRepo,
        ApprovalDecisionRepository decisionRepo) {
        var approvers = approverRepo.findByApprovalProposal(proposal);
        
        var approverResponses = approvers.stream().map(approver -> {
            var decisions = decisionRepo.findByApprovalApproverOrderByDecidedAtDesc(approver);
            return DecisionResponseByApproverList.from(approver, decisions);
        }).collect(Collectors.toList());
        
        return DecisionResponseByFilteredApprover.from(proposal, approverResponses);
    }
}
