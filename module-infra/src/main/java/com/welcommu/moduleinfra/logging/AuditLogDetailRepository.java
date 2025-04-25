package com.welcommu.moduleinfra.logging;


import com.welcommu.moduledomain.logging.AuditLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogDetailRepository extends JpaRepository<AuditLogDetail, Long> {
}
