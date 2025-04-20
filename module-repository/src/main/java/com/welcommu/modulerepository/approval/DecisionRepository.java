package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecisionRepository extends JpaRepository<ApprovalDecision, Long> {

    List<ApprovalDecision> findByApprovalProposal(ApprovalProposal approvalProposal);
}
