package com.welcommu.moduleapi.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log")
public class LogTestController {

    @GetMapping
    public String testLog() {
        log.info("🔥 INFO 로그 테스트입니다");
        log.debug("🐞 DEBUG 로그 테스트입니다");
        log.warn("⚠️ WARN 로그 테스트입니다");
        return "로그 전송 완료";
    }
}
