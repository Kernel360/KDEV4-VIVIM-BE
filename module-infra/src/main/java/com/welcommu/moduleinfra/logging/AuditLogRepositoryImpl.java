package com.welcommu.moduleinfra.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<AuditLog> findByConditions(
        ActionType actionType,
        TargetType targetType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long userId
    ) {
        StringBuilder sb = new StringBuilder("SELECT a FROM AuditLog a WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (actionType != null) {
            sb.append(" AND a.actionType = :actionType");
            params.put("actionType", actionType);
        }
        if (targetType != null) {
            sb.append(" AND a.targetType = :targetType");
            params.put("targetType", targetType);
        }
        if (startDate != null) {
            sb.append(" AND a.loggedAt >= :startDate");
            params.put("startDate", startDate);
        }
        if (endDate != null) {
            sb.append(" AND a.loggedAt <= :endDate");
            params.put("endDate", endDate);
        }
        if (userId != null) {
            sb.append(" AND a.actorId = :userId");
            params.put("userId", userId);
        }

        TypedQuery<AuditLog> query = em.createQuery(sb.toString(), AuditLog.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }
}

