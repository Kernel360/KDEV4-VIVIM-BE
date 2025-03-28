package com.welcommu.modulerepository.projectprogress;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {

     float findMaxPositionByProjectId(Long projectId);
     List<ProjectProgress> findByProject(Project project);
}
