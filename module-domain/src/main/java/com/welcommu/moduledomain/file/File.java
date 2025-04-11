package com.welcommu.moduledomain.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name="file")
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

    @Column(name="reference_id", nullable = false)
    private Long referenceId;

    public static File create(String fileName, String fileUrl, Long fileSize, ReferenceType referenceType, Long referenceId) {
        return File.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }


}