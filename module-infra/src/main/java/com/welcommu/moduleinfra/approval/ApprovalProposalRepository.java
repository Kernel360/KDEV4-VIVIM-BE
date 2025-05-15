package com.welcommu.moduleinfra.approval;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalProposalRepository extends JpaRepository<ApprovalProposal, Long> {

    List<ApprovalProposal> findByProjectProgress(ProjectProgress progress);

    Long countByProjectProgressIdAndProposalStatus(Long id,
        ApprovalProposalStatus approvalProposalStatus);

    Long countByProjectProgressId(Long id);

    @Query("""
            SELECT ap
            FROM ApprovalProposal ap
            WHERE ap.projectProgress.project.id IN :projectIds
            ORDER BY ap.createdAt DESC
        """)
    List<ApprovalProposal> findRecentProposalsByProjectIds(
        @Param("projectIds") List<Long> projectIds, Pageable pageable);

    List<ApprovalProposal> findTop5ByOrderByCreatedAtDesc();
}
