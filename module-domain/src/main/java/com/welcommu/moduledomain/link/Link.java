package com.welcommu.moduledomain.link;

import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name="link")
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Column(name="reference_id", nullable = false)
    private Long referenceId;

    public static Link createLink(String title, String url, ReferenceType referenceType, Long referenceId) {
        return Link.builder()
                .title(title)
                .url(url)
                .createdAt(LocalDateTime.now())
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

}
