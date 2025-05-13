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
    private int currentPage;   // 추가
    private int totalPages;    // 추가

//    @Getter
//    @AllArgsConstructor
//    public static class Cursor {
//        private final String loggedAt;
//        private final Long id;
//    }
}