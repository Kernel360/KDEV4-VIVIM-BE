package com.welcommu.moduleservice.logging;

public interface AuditableService<T> {
    void createAuditLog(T entity, Long userId);
    void modifyAuditLog(T before, T after, Long userId);
    void deleteAuditLog(T entity, Long userId);
}
