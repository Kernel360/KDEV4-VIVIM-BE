package com.welcommu.moduledomain.checklistApprove;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistApproveRepository extends JpaRepository<ChecklistApprove, Long> {}
