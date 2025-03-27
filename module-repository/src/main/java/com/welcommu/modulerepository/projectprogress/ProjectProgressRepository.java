package com.welcommu.modulerepository.projectprogress;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {

     float findMaxPositionByProjectId(Long projectId);
}
