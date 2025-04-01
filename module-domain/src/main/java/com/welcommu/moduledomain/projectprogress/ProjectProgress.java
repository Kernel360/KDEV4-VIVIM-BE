package com.welcommu.moduledomain.projectprogress;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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

    @Column(nullable = false)
    private String name;
    private float position;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    @OneToOne
    private User user;

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
}
