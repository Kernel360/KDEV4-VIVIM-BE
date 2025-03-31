package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    List<ProjectUser> findByUser(User user);
    void deleteByProject(Project project);
    Optional<ProjectUser> findByProjectIdAndUserId(Long projectId, Long userId);
    Optional<ProjectUser> findByUserAndProject(User user, Project project);
}
