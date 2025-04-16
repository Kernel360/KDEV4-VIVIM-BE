package com.welcommu.moduledomain.logging;

import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 행위를 지시한 useId
    private Long actorId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    // 해당 작업이 일어난 대상의 id
    private Long targetId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private LocalDateTime loggedAt;

    @Builder.Default
    @OneToMany(mappedBy = "auditLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLogDetail> details = new ArrayList<>();

    public void addDetail(String fieldName, String oldValue, String newValue) {
        AuditLogDetail detail = AuditLogDetail.builder()
                .fieldName(fieldName)
                .oldValue(oldValue)
                .newValue(newValue)
                .auditLog(this)
                .build();
        this.details.add(detail);
    }
}
