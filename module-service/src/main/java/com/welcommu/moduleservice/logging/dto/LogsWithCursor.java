package com.welcommu.moduleservice.logging.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LogsWithCursor<T> {
    private final List<T> logs;
    private final Cursor next;
    private int currentPage;
    private int totalPages;
}