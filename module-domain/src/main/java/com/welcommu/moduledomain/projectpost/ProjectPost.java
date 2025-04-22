package com.welcommu.moduledomain.projectpost;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "project_posts")
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

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_post_status")
    private ProjectPostStatus projectPostStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "writer_ip")
    private String writerIp;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creator_role")
    private String creatorRole;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "response_to_question")
    private String responseToQuestion;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setProjectPostStatus(ProjectPostStatus status) {
        this.projectPostStatus = status;
    }

    public void setModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void setResponseToQuestion(String responseToQuestion) {
        this.responseToQuestion = responseToQuestion;
    }
}
