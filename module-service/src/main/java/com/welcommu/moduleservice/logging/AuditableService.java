package com.welcommu.moduleservice.logging;

public interface AuditableService<T> {
    void logCreateAudit(T entity, Long userId);
    void logUpdateAudit(T before, T after, Long userId);
    void logDeleteAudit(T entity, Long userId);
}
