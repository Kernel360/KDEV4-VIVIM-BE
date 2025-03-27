package com.welcommu.moduledomain.projectpost.entity;

import java.time.LocalDateTime;

import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import jakarta.persistence.*;
import lombok.*;



@Table(name="project_posts")
@Getter
@Setter
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
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
}
