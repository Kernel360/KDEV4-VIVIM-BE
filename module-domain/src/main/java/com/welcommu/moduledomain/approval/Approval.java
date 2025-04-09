package com.welcommu.moduledomain.checklist;

import com.welcommu.moduledomain.checklist.Checklist;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "checklist_approves")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus approveStatus = ApprovalStatus.IN_PROGRESS;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    // TODO softDelete 적용 예정
//    public boolean isDeleted(LocalDateTime deletedAt)
//        return this.deletedAt != null;
//    }
}

