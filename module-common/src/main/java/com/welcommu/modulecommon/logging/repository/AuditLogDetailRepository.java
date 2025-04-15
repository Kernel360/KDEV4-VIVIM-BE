package com.welcommu.modulecommon.logging.repository;

import com.welcommu.modulecommon.logging.entity.AuditLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogDetailRepository extends JpaRepository<AuditLogDetail, Long> {
}
