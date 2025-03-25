package com.welcommu.moduledomain.projectprogress;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProgress {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private float order;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    @OneToOne
    private User creator;

    @OneToOne
    private User modifier;

    @OneToOne
    private User deleter;

    @ManyToOne
    private Project project;

    // @PrePersist: 엔티티가 저장되기 전 호출됨
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // createdAt에 현재 시간 설정
        }
    }

    // @PreUpdate: 엔티티가 수정되기 전 호출됨
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now(); // modifiedAt에 현재 시간 설정
    }

    // 논리 삭제 처리
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now(); // deletedAt에 현재 시간 설정
    }
}
