package com.welcommu.moduleinfra.approval;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalProposalRepository extends JpaRepository<ApprovalProposal, Long> {

    List<ApprovalProposal> findByProjectProgress(ProjectProgress progress);
}
