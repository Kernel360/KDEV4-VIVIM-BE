package com.welcommu.moduleservice.user;

import static org.junit.jupiter.api.Assertions.*;

import com.welcommu.moduleapi.ModuleApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(classes = ModuleApiApplication.class)
class PasswordResetServiceTest {

    @Autowired
    JavaMailSender mailSender;

    @Test
    public void testSend() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("--");
        msg.setSubject("테스트 메일");
        msg.setText("Gmail SMTP 설정 확인 중입니다.");
        mailSender.send(msg);
    }
}