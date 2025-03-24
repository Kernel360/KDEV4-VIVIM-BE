package com.welcommu.moduledomain.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    private Boolean isDeleted;

    // 🔹 참가자 연관관계
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    // 🔹 담당자 연관관계
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectManager> projectManagers = new ArrayList<>();

    @Builder
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    // 🔸 설명 변경
    public void updateDescription(String description) {
        this.description = description;
        this.modifiedAt = LocalDateTime.now();
    }

    // 🔸 삭제 처리
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 🔸 연관 엔티티 추가 메서드
    public void addUserProject(UserProject userProject) {
        this.userProjects.add(userProject);
    }

    public void addManager(ProjectManager manager) {
        this.projectManagers.add(manager);
    }
}
