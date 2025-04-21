package com.welcommu.modulerepository.logging;


import com.welcommu.moduledomain.logging.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTargetId(Long targetId);
}
