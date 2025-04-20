package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.modulerepository.approval.ApproverRepository;
import com.welcommu.modulerepository.approval.ProposalRepository;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApproverService {

    private final ApproverRepository approverRepository;
    private final ProposalRepository proposalRepository;

    public List<ApproverResponse> getAllApprover(Long approvalId) {

        ApprovalProposal proposal = proposalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("해당 승인요청이 존재하지 않습니다."));

        return approverRepository.findByApprovalProposal(proposal).stream()
            .map(ApproverResponse::of)
            .collect(Collectors.toList());
    }
}
