package com.welcommu.moduleinfra.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {

    List<ProjectPost> findAllByProjectIdAndDeletedAtIsNull(Long projectId);

    List<ProjectPost> findTop5RecentPostsByDeletedAtIsNullOrderByCreatedAtDesc();

    List<ProjectPost> findTop5ByProject_IdInOrderByCreatedAtDesc(List<Long> projectIds);
}
