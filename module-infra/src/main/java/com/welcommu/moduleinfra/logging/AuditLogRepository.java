package com.welcommu.moduleinfra.logging;


import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, AuditLogRepositoryCustom  {
    List<AuditLog> findByTargetId(Long targetId);

    @Query("SELECT DISTINCT a FROM AuditLog a LEFT JOIN FETCH a.details")
    List<AuditLog> findAllWithDetails();

    // (A) 필터만 적용한 총 개수
    @Query("""
        select count(a)
          from AuditLog a
         where (:actionType   is null or a.actionType  = :actionType)
           and (:targetType   is null or a.targetType  = :targetType)
           and (:start        is null or a.loggedAt   >= :start)
           and (:end          is null or a.loggedAt   <= :end)
           and (:userId       is null or a.actorId    = :userId)
    """)
    long countByFilters(
        @Param("actionType") ActionType actionType,
        @Param("targetType") TargetType targetType,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("userId") Long userId
    );

    // (B) 커서 이전(더 최신) 로그 개수
    @Query("""
        select count(a)
          from AuditLog a
         where (:actionType   is null or a.actionType  = :actionType)
           and (:targetType   is null or a.targetType  = :targetType)
           and (:start        is null or a.loggedAt   >= :start)
           and (:end          is null or a.loggedAt   <= :end)
           and (:userId       is null or a.actorId    = :userId)
           and (   a.loggedAt >  :cursorLoggedAt
                or (a.loggedAt = :cursorLoggedAt and a.id > :cursorId)
               )
    """)
    long countBeforeCursor(
        @Param("actionType")    ActionType actionType,
        @Param("targetType")    TargetType targetType,
        @Param("start")         LocalDateTime start,
        @Param("end")           LocalDateTime end,
        @Param("userId")        Long userId,
        @Param("cursorLoggedAt") LocalDateTime cursorLoggedAt,
        @Param("cursorId")       Long cursorId
    );

    @Query("""
      select a.id
        from AuditLog a
       where (:actionType is null or a.actionType = :actionType)
         and (:targetType is null or a.targetType = :targetType)
         and (:start      is null or a.loggedAt  >= :start)
         and (:end        is null or a.loggedAt  <= :end)
         and (:userId     is null or a.actorId   = :userId)
    """)
    List<Long> findIdsByFilters(
        ActionType actionType,
        TargetType targetType,
        LocalDateTime start,
        LocalDateTime end,
        Long userId,
        Pageable pageable
    );
}
