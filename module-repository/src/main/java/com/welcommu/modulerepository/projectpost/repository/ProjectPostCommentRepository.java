package com.welcommu.modulerepository.projectpost.repository;

import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Long> {
}
