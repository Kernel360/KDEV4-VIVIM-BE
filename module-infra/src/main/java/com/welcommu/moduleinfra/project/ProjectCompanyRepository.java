package com.welcommu.moduleinfra.project;

import com.welcommu.moduledomain.projectCompany.ProjectCompany;
import com.welcommu.moduledomain.project.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCompanyRepository extends JpaRepository<ProjectCompany, Long> {
    void deleteByProject(Project project);
    List<ProjectCompany> findByProjectId(Long projectId);

}
