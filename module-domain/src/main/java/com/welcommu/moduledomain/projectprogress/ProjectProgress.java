package com.welcommu.moduledomain.projectprogress;

import com.welcommu.moduledomain.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
    name = "project_progress",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_project_progress_project_id_name",
            columnNames = {"project_id", "name"}
        ),
        @UniqueConstraint(
            name = "uk_project_progress_project_id_position",
            columnNames = {"project_id", "position"}
        )
    }
)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private Float position;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
