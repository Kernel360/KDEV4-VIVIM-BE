package com.welcommu.moduleservice.logging.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogsWithCursor<T> {
    private final List<T> logs;
    private final Cursor nextCursor;

    @Getter
    @AllArgsConstructor
    public static class Cursor {
        private final String loggedAt;
        private final Long id;
    }
}