package com.welcommu.modulerepository.checklist;


import com.welcommu.moduledomain.checklist.Checklist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    List<Checklist> findByProjectProgressId(Long progressId);
}
