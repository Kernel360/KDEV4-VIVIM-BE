package com.welcommu.moduledomain.checklist;


import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Table(name = "checklists")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "project_progress_id")
    private ProjectProgress projectProgress;

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted(LocalDateTime deletedAt) {
        return this.deletedAt != null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Checklist createChecklist(ProjectProgress progress, String name) {
        return Checklist.builder()
            .name(name)
            .createdAt(LocalDateTime.now())
            .projectProgress(progress)
            .build();
    }
}
