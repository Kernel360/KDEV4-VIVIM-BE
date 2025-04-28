package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "approval_proposals")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 승인권자 카운팅
    private int countTotalApprover;
    private int countApprovedApprover;
    private boolean isProposalSent;
    private boolean isAllApproved;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalProposalStatus proposalStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "progress_id")
    private ProjectProgress projectProgress;

    public void markProposalSent() {
        this.isProposalSent = true;
        this.proposalStatus = ApprovalProposalStatus.UNDER_REVIEW;
        this.modifiedAt = LocalDateTime.now();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setProposalStatus(ApprovalProposalStatus status) {
        this.proposalStatus = status;
        this.modifiedAt = LocalDateTime.now();
    }

    public void setCountTotalApprover(int size) {
        this.countTotalApprover = size;
    }
}
