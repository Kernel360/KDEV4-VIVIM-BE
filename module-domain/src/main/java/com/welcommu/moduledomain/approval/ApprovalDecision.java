package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "approval_decision")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime decidedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalDecisionStatus decisionStatus;

    // 승인권자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approval_approver_id")
    private ApprovalApprover approvalApprover;

    public void approve() {
        this.decisionStatus = ApprovalDecisionStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();
    }

    public void reject() {
        this.decisionStatus = ApprovalDecisionStatus.REJECTED;
        this.decidedAt = LocalDateTime.now();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}