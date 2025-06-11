package com.example.studentmanagement.designpattern.observer;

import com.example.studentmanagement.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TeacherEmailNotifier implements Subscriber {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void update(String eventType, Object data, String message) {
        if ("SCORE_BATCH_UPDATED".equals(eventType) && data instanceof Account) {
            Account account = (Account) data;
            if (account.getEmail() != null) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(account.getEmail());
                mailMessage.setSubject("Xác nhận cập nhật điểm số - Hệ thống Quản lý Học sinh");
                mailMessage.setText(
                    "Chào " + account.getFullName() + ",\n\n" +
                    "Bạn đã thực hiện cập nhật điểm số thành công:\n" +
                    message + "\n\n" +
                    "Vui lòng kiểm tra lại trên hệ thống Quản lý Học sinh nếu cần thiết.\n" +
                    "Nếu có thắc mắc, xin vui lòng liên hệ support@studentmanagement.com hoặc số 0123-456-789.\n\n" +
                    "Trân trọng,\n" +
                    "Hệ thống Quản lý Học sinh"
                );
                mailSender.send(mailMessage);
            }
        }
    }
}