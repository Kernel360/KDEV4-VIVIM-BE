package com.welcommu.modulerepository.approval;

import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByChecklist(Checklist checklist);
}
