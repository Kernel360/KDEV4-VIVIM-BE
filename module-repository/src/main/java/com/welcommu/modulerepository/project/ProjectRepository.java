package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
