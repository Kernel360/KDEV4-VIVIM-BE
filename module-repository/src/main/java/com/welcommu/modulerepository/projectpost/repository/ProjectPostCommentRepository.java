package com.welcommu.modulerepository.projectpost.repository;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Long> {
    List<ProjectPostComment> findAllByProjectPostIdAndDeletedAtIsNull(Long ProjectpostId);
}
