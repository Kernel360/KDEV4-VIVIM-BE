package com.welcommu.moduleservice.user;

public interface MailService {
    /**
     * 비동기로 이메일을 보냅니다.
     *
     * @param to       받는 사람 이메일
     * @param subject  메일 제목
     * @param htmlBody 메일 본문(HTML)
     */
    void sendEmail(String to, String subject, String htmlBody);
}
