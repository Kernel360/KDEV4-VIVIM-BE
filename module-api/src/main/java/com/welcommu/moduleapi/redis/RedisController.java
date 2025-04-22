package com.welcommu.moduleapi.redis;

import com.welcommu.moduleservice.redis.RedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@Tag(name = "Redis 연동 테스트용  API", description = "Redis 연동 테스트용 API 입니다.(추후 제거 예정).")

public class RedisController {

    private final RedisService redisTestService;

    @PostMapping("/save")
    public String save(@RequestParam String key, @RequestParam String value) {
        redisTestService.saveData(key, value, 60); // 60초 TTL
        return "저장 완료";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisTestService.getData(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisTestService.deleteData(key);
        return "삭제 완료";
    }
}
