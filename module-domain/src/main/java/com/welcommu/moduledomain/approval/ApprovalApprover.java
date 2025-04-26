package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.projectUser.ProjectUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectUser projectUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approval_proposal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApprovalProposal approvalProposal;
}
