package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalDecisionRepository extends JpaRepository<ApprovalDecision, Long> {

}
