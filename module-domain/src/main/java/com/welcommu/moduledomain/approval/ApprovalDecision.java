package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table
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
    private ApprovalDecisionStatus approvalStatus;

    @ManyToOne
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approver_id")
    private ApprovalApprover approver;
}