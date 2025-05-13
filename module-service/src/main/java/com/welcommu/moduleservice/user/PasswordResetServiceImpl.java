package com.welcommu.moduleservice.user;

import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService{
    private final StringRedisTemplate redis;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static final long CODE_TTL_MINUTES = 15;
    private static final String KEY_PREFIX = "pw-reset:";

    // 1) 요청 처리: 인증번호 생성 → Redis 저장 → 이메일 발송
    @Transactional
    public void requestReset(String email) {
        // 1.1. 사용자 존재 여부 확인
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // 1.2. Redis에 토큰 저장 (중복 요청 시 덮어쓰기)
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        String key  = KEY_PREFIX + email;
        redis.opsForValue().set(key, code, CODE_TTL_MINUTES, TimeUnit.MINUTES);

        // 1.3. 이메일로 토큰 발송
        try {
            sendEmail(email, code);
        } catch (MessagingException e) {
            // 발송 실패 시 Redis에 저장된 토큰 삭제
            redis.delete(key);
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    private void sendEmail(String to, String code) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setTo(to);
        helper.setSubject("비밀번호 초기화 인증번호 안내");
        helper.setText(
            "<p>안녕하세요!</p>" +
                "<p>프로젝트 매니저 비빔입니다!</p>" +
                "<p>비밀번호 초기화를 위한 인증번호는 <b>" + code + "</b> 입니다.</p>" +
                "<p>15분 이내에 입력해주세요.</p>",
            true
        );
        mailSender.send(msg);
    }

    // 2) 확인 & 비밀번호 변경
    @Transactional
    public boolean confirmReset(String email, String code, String newPassword) {
        String key = KEY_PREFIX + email;
        String saved = redis.opsForValue().get(key);
        if (saved == null || !saved.equals(code)) {
            return false;
        }

        // 2.1. 비밀번호 업데이트
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // 2.2. Redis 토큰 삭제
        redis.delete(key);
        return true;
    }
}
