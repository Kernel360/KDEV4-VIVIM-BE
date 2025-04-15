package com.welcommu.modulerepository.logging;


import com.welcommu.moduledomain.logging.AuditLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogDetailRepository extends JpaRepository<AuditLogDetail, Long> {
}
