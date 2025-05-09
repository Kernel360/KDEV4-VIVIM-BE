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

    @Query("SELECT p FROM Project p " +
        "WHERE p.isDeleted = false " +                 // 삭제되지 않은 프로젝트
        "AND p.projectStatus != 'COMPLETED'" +
        "ORDER BY p.endDate ASC")          // 완료되지 않은 프로젝트
    List<Project> findNonCompletedProjectsOrderedByEndDate();

}
