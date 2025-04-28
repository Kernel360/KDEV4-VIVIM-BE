package com.welcommu.moduledomain.file;

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

@Table(name = "file")
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName; // 실제 파일명

    @Column(name = "file_url", nullable = false, columnDefinition = "TEXT")
    private String fileUrl; // S3나 서버 경로

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }


}