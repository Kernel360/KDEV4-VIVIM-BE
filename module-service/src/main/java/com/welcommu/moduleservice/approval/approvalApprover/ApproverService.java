package com.welcommu.moduleservice.approval.approvalApprover;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApproverRepository;
import com.welcommu.modulerepository.approval.ProposalRepository;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
