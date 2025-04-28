package com.welcommu.moduleinfra.approval;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalDecisionRepository extends JpaRepository<ApprovalDecision, Long> {

    List<ApprovalDecision> findByApprovalApproverId(Long approverId);
    List<ApprovalDecision> findByApprovalApproverIdIn(List<Long> approverIds);

    List<ApprovalDecision> findByApprovalApprover(ApprovalApprover approver);
}
