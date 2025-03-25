package com.welcommu.modulerepository.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
}
