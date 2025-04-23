package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProjectRepositoryCustom {
    Page<Project> searchByConditions(String name, String description, Boolean isDeleted, Pageable pageable);
    List<Project> findAllByCompanyId(Long companyId);
    List<Object[]> findAllByCompanyIdWithMyRole(Long companyId, Long myUserId);
}
