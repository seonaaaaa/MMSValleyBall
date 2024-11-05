package com.team.MMSValleyBall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // 메일 수신자
        message.setSubject(subject); // 메일 제목
        message.setText(text); // 메일 본문 내용
//        message.setFrom("yami78999@gmail.com");  // 발신자 이메일 주소 설정

        mailSender.send(message);  // 이메일 전송
    }
}
