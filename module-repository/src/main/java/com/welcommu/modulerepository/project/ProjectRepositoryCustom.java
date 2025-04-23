package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import java.util.List;

public interface ProjectRepositoryCustom {
    List<Project> findAllByCompanyId(Long companyId);
    List<Object[]> findAllByCompanyIdWithMyRole(Long companyId, Long myUserId);
}
