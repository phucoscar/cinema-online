package com.vukimphuc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSenderImpl sender;

    public void sendMailRegister(String email, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Chào mừng bạn tới với hệ thống đặt vé xem phim online của chúng tôi!");
        message.setText("Dưới đây là thông tin đăng nhập của bạn: \n" +
                "Username: " + username + "\n" +
                "Mật khẩu: " + password);
        sender.send(message);
    }

    public void sendEmailResetPassword(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Khôi phục mật khẩu");
        message.setText("Mật khẩu mới của bạn là: " + password);
        sender.send(message);
    }
}
