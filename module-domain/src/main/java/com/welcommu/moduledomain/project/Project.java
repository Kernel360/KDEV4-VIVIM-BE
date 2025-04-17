package com.welcommu.moduledomain.project;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Project snapshot() {
        return Project.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .isDeleted(this.isDeleted)
                .build();
    }
}
