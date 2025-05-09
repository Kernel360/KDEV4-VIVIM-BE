package com.welcommu.moduleinfra.project;

import com.welcommu.moduledomain.project.Project;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {

    Project findByIdAndIsDeletedFalse(Long projectId);

    List<Project> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);

}
