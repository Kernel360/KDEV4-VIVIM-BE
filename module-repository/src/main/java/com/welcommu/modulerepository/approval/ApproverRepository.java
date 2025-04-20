package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverRepository extends JpaRepository<ApprovalApprover, Long> {

    // projectUser.user 로 내부 탐색
    Optional<ApprovalApprover> findByApprovalProposalAndProjectUserUser(ApprovalProposal proposal,
        User user);

    List<ApprovalApprover> findByApprovalProposal(ApprovalProposal proposal);

    boolean existsByApprovalProposal(ApprovalProposal proposal);
}
