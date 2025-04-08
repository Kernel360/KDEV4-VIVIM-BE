package com.welcommu.modulerepository.checklist;

import com.welcommu.moduledomain.checklist.ChecklistApprove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistApproveRepository extends JpaRepository<ChecklistApprove, Long> {

}
