package com.welcommu.modulerepository.logging;


import com.welcommu.moduledomain.logging.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, AuditLogRepositoryCustom  {
    List<AuditLog> findByTargetId(Long targetId);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.details")
    List<AuditLog> findAllWithDetails();
}
