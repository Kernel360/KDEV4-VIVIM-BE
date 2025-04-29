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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<AuditLog> findByConditions(
        ActionType actionType,
        TargetType targetType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long userId,
        Pageable pageable
    ) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT a FROM AuditLog a LEFT JOIN FETCH a.details WHERE 1=1");
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(DISTINCT a) FROM AuditLog a WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (actionType != null) {
            jpql.append(" AND a.actionType = :actionType");
            countJpql.append(" AND a.actionType = :actionType");
            params.put("actionType", actionType);
        }
        if (targetType != null) {
            jpql.append(" AND a.targetType = :targetType");
            countJpql.append(" AND a.targetType = :targetType");
            params.put("targetType", targetType);
        }
        if (startDate != null) {
            jpql.append(" AND a.loggedAt >= :startDate");
            countJpql.append(" AND a.loggedAt >= :startDate");
            params.put("startDate", startDate);
        }
        if (endDate != null) {
            jpql.append(" AND a.loggedAt <= :endDate");
            countJpql.append(" AND a.loggedAt <= :endDate");
            params.put("endDate", endDate);
        }
        if (userId != null) {
            jpql.append(" AND a.actorId = :userId");
            countJpql.append(" AND a.actorId = :userId");
            params.put("userId", userId);
        }

        jpql.append(" ORDER BY a.loggedAt DESC");

        TypedQuery<AuditLog> query = em.createQuery(jpql.toString(), AuditLog.class);
        params.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<AuditLog> results = query.getResultList();

        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);
        params.forEach(countQuery::setParameter);
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
    @Override
    public List<Long> findIdsWithCursor(
        ActionType actionType,
        TargetType targetType,
        LocalDateTime start,
        LocalDateTime end,
        Long userId,
        LocalDateTime cursorLoggedAt,
        Long cursorId,
        int size
    ) {
        // 1) JPQL 빌드
        StringBuilder jpql = new StringBuilder("SELECT a.id FROM AuditLog a WHERE 1=1 ");
        if (actionType != null)   jpql.append("AND a.actionType = :actionType ");
        if (targetType != null)   jpql.append("AND a.targetType = :targetType ");
        if (start != null)        jpql.append("AND a.loggedAt >= :start ");
        if (end != null)          jpql.append("AND a.loggedAt <= :end ");
        if (userId != null)       jpql.append("AND a.actorId = :userId ");
        if (cursorLoggedAt != null && cursorId != null) {
            jpql.append(
                "AND (a.loggedAt < :cursorLoggedAt " +
                    "OR (a.loggedAt = :cursorLoggedAt AND a.id < :cursorId)) "
            );
        }
        jpql.append("ORDER BY a.loggedAt DESC, a.id DESC");

        // 2) TypedQuery 생성 및 파라미터 세팅
        TypedQuery<Long> q = em.createQuery(jpql.toString(), Long.class);
        if (actionType != null)   q.setParameter("actionType", actionType);
        if (targetType != null)   q.setParameter("targetType", targetType);
        if (start != null)        q.setParameter("start", start);
        if (end != null)          q.setParameter("end", end);
        if (userId != null)       q.setParameter("userId", userId);
        if (cursorLoggedAt != null && cursorId != null) {
            q.setParameter("cursorLoggedAt", cursorLoggedAt);
            q.setParameter("cursorId", cursorId);
        }

        // 3) 페이징 크기만큼 결과 제한
        q.setMaxResults(size);

        return q.getResultList();
    }
    @Override
    public List<AuditLog> findWithDetailsByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        // DISTINCT 꼭 추가
        String jpql =
            "SELECT DISTINCT a FROM AuditLog a " +
                "LEFT JOIN FETCH a.details d " +
                "WHERE a.id IN :ids " +
                "ORDER BY a.loggedAt DESC, a.id DESC";

        return em.createQuery(jpql, AuditLog.class)
            .setParameter("ids", ids)
            .getResultList();
    }
}

