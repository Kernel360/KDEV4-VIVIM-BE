package com.welcommu.modulerepository.projectpost.repository;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
    List<ProjectPost> findAllByProjectIdAndDeletedAtIsNull(Long projectId);
}
