package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.projectUser.ProjectUser;
import jakarta.persistence.Entity;
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

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
ApprovalApprover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_user_id")
    private ProjectUser projectUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approval_proposal_id")
    private ApprovalProposal approvalProposal;
}
