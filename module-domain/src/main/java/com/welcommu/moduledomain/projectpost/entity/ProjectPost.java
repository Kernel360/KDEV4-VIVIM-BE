package com.welcommu.moduledomain.projectpost.entity;

import java.time.LocalDateTime;

import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import jakarta.persistence.*;
import lombok.*;



@ToString
@Table(name="project_posts")
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_post_status")
    private ProjectPostStatus projectPostStatus;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @Column(name="writer_ip")
    private String writerIp;

    @Column(name="parent_id")
    private Long parentId;

    @Column(name="creator_id")
    private Long creatorId;

    @Column(name="modifier_id")
    private Long modifierId;

    @Column(name="deleter_id")
    private Long deleterId;

    @Column(name="project_id")
    private Long projectId;

    public void modify(String title, String content, ProjectPostStatus projectPostStatus) {
        this.title = title;
        this.content = content;
        this.projectPostStatus = projectPostStatus;
    }

    public void delete(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        this.isDeleted = true;
    }
}
