package com.welcommu.modulerepository.projectprogress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectProgressRepository extends JpaRepository<com.welcommu.moduledomain.projectprogress.ProjectProgress, Long> {

}
