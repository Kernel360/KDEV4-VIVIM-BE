package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

}
