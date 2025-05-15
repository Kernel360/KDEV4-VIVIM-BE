package com.welcommu.moduleservice.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService{
    private final JavaMailSender mailSender;

    @Retryable(
        value = { MailSendException.class },   // 재시도 대상 예외
        maxAttempts = 3,                       // 총 3번 시도
        backoff = @Backoff(delay = 5000, multiplier = 2)
    )

    @Async
    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(msg);
        } catch (MessagingException e) {
            log.error("메일 전송 실패(to: {})", to, e);
        }
    }

    @Recover
    public void recover(MailSendException e, String to, String subject, String htmlBody) {
        log.error("메일 전송 재시도 모두 실패: to={}, subject={}", to, subject, e);
        // TODO: DB에 실패 로그 남기기, 관리자 알림
    }
}
