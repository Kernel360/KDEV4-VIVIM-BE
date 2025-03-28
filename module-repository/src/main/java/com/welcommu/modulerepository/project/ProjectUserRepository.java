package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    // 1. 프로젝트 + 역할 기준으로 참여자 조회
    List<ProjectUser> findByProjectAndProjectUserManageRole(Project project, ProjectUserManageRole role);

    // 2. 프로젝트 기준으로 전체 참여자 조회
    List<ProjectUser> findByProject(Project project);

    // 3. 유저 기준으로 소속 프로젝트 전체 조회
    List<ProjectUser> findByUser(User user);

    // 4. 프로젝트 기준으로 전체 참여자 삭제
    void deleteByProject(Project project);
}
