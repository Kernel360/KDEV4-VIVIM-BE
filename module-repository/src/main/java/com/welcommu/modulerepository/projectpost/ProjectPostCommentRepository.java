package com.welcommu.modulerepository.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Long> {
}
