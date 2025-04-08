package com.welcommu.moduledomain.projectpost;

import com.welcommu.moduledomain.company.CompanyRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;



@Table(name = "project_post_comments")
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "writer_ip", length = 50)
    private String writerIp;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name="creator_name")
    private String creatorName;

    @Column(name="creator_role")
    private String creatorRole;

    @Column(name = "project_post_id", nullable = false)
    private Long projectPostId;

    @Column(name = "parent_id")
    private Long parentId;

    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) {}
    public void setModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}
