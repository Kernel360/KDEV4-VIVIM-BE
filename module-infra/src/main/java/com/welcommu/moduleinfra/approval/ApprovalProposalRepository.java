package com.welcommu.moduleinfra.approval;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalProposalRepository extends JpaRepository<ApprovalProposal, Long> {

    List<ApprovalProposal> findByProjectProgress(ProjectProgress progress);

    Long countByProjectProgressIdAndProposalStatus(Long id, ApprovalProposalStatus approvalProposalStatus);

    Long countByProjectProgressId(Long id);

    List<ApprovalProposal> findByProjectProgress_Project_IdInOrderByCreatedAtDesc(List<Long> projectIds, PageRequest createdAt);
}
