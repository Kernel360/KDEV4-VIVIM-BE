package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalApproverRepository extends JpaRepository<ApprovalApprover, Long> {

    List<ApprovalApprover> findByApprovalProposalIn(List<ApprovalProposal> approvalProposalList);

    List<ApprovalApprover> findByApprovalProposal(ApprovalProposal approvalProposal);
}
