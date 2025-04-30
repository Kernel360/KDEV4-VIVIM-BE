package com.welcommu.moduleservice.logging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cursor {
    private final String loggedAt;
    private final Long id;
}
