package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.projectUser.ProjectUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalApprover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private ApprovalApproverStatus approverStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectUser projectUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approval_proposal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApprovalProposal approvalProposal;

    public void modifyApproverStatus(List<ApprovalDecision> decisions) {
        if (decisions.isEmpty()) {
            if (!approvalProposal.isProposalSent()) {
                this.approverStatus = ApprovalApproverStatus.BEFORE_REQUEST;
            } else {
                this.approverStatus = ApprovalApproverStatus.WAITING_FOR_RESPONSE;
            }
            return;
        }

        boolean hasApproved = decisions.stream()
            .anyMatch(d -> d.getDecisionStatus() == ApprovalDecisionStatus.APPROVED);

        if (hasApproved) {
            this.approverStatus = ApprovalApproverStatus.COMPLETE_APPROVED;
        } else {
            this.approverStatus = ApprovalApproverStatus.REQUEST_MODIFICATION;
        }
    }
}
