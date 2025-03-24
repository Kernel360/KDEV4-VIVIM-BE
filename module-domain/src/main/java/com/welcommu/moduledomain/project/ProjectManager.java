package com.welcommu.moduledomain.project;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_managers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 담당자 역할
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 실제 유저 ID (user 테이블과의 관계는 필요에 따라)
    @Column(name = "project_manager_id", nullable = false)
    private Long projectManagerId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 프로젝트 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder
    public ProjectManager(Role role, Long projectManagerId, Project project) {
        this.role = role;
        this.projectManagerId = projectManagerId;
        this.project = project;
        this.createdAt = LocalDateTime.now();
    }

    public enum Role {
        CLIENT,
        DEVELOPER
    }
}