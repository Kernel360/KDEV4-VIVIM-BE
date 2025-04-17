package com.welcommu.moduledomain.approval;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
public class ApprovalProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private String modifiedAt;
    private LocalDateTime deletedAt;

    // 승인권자 카운팅
    private int countTotalApprovers;
    private int countApprovedApprovers;
    private boolean isAllApproved;

    @OneToOne
    private User writer;

    @ManyToOne
    @JoinColumn(name = "project_progress_id")
    private ProjectProgress progress;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 승인자 상태가 APPROVED 인 경우를 읽어들여 카운팅
    public void countApprovedApprovers(ApprovalStatus status) {
        if (status == ApprovalStatus.APPROVED) {
            this.countApprovedApprovers++;
        }
        if (this.countTotalApprovers == this.countApprovedApprovers) {
            this.isAllApproved = true;
        }
    }

}
