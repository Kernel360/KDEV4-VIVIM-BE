package com.welcommu.modulerepository.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {

    List<ProjectPost> findAllByProjectIdAndDeletedAtIsNull(Long projectId);

    List<ProjectPost> findTop5ByProjectIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long projectId);

    List<ProjectPost> findTop5ByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("SELECT p FROM ProjectPost p WHERE p.deletedAt IS NULL AND p.projectId IN :projectIds ORDER BY p.createdAt DESC")
    List<ProjectPost> findTop5PostsByProjectIds(List<Long> projectIds);
}
