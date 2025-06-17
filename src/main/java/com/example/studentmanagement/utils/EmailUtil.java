package com.example.studentmanagement.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j // Logger của Lombok
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // 'true' cho multipart, 'UTF-8' cho encoding

            helper.setFrom(senderEmail);
            helper.setTo(toEmail.split(",")); // Xử lý nhiều người nhận
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // 'true' indicates that this is an HTML email.

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to: {} with subject: {}", toEmail, subject);
        } catch (MessagingException e) {
            log.error("Error sending email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public String createWarningEmailBody(String studentName, String violationName, LocalDate violationDate, String customMessage) {
        String footerMessage = "Email này được tạo tự động từ hệ thống quản lý học sinh. Vui lòng không trả lời.";
        String schoolName = "Hệ thống Quản lý Học sinh XYZ"; // Tên trường học/tổ chức của bạn

        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                ".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                ".header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }" +
                ".header h2 { color: #333333; margin: 0; }" +
                ".content { padding: 20px 0; }" +
                ".content p { line-height: 1.6; color: #555555; }" +
                ".highlight { font-weight: bold; color: #d9534f; }" + // Màu đỏ cho cảnh báo
                ".info-box { background-color: #f9f9f9; border-left: 4px solid #f0ad4e; padding: 15px; margin: 15px 0; border-radius: 4px; }" + // Màu cam cho box thông tin
                ".footer { text-align: center; padding-top: 20px; border-top: 1px solid #eeeeee; font-size: 12px; color: #aaaaaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "    <div class=\"header\">" +
                "        <h2>THÔNG BÁO CẢNH BÁO VI PHẠM KỶ LUẬT HỌC SINH</h2>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <p>Kính gửi Quý phụ huynh/cố vấn học tập,</p>" +
                "        <p>Chúng tôi xin thông báo về một trường hợp vi phạm kỷ luật của học sinh tại " + schoolName + ".</p>" +
                "        <div class=\"info-box\">" +
                "            <p><strong>Thông tin vi phạm:</strong></p>" +
                "            <ul>" +
                "                <li>Học sinh: <span class=\"highlight\">" + studentName + "</span></li>" +
                "                <li>Loại vi phạm: <span class=\"highlight\">" + violationName + "</span></li>" +
                "                <li>Ngày vi phạm: <span class=\"highlight\">" + violationDate + "</span></li>" +
                "            </ul>" +
                "        </div>" +
                "        <p>" + customMessage + "</p>" +
                "        <p>Chúng tôi mong Quý phụ huynh/cố vấn học tập lưu ý và cùng phối hợp với nhà trường để giúp học sinh cải thiện.</p>" +
                "        <p>Trân trọng,</p>" +
                "        <p>" + schoolName + "</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>" + footerMessage + "</p>" +
                "    </div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public String createGenericBody(String username, String message) {
        return "<html>" +
                "<head>" +
                "<!-- Fonts -->" +
                "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">" +
                "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Itim&display=swap\" rel=\"stylesheet\">" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap\" rel=\"stylesheet\">" +
                "<style>" +
                "body {" +
                "font-family: \"Inter\", sans-serif;" +
                "margin: 0;" +
                "padding: 0;" +
                "box-sizing: border-box;" +
                "}" +
                ".container {" +
                "padding: 20px;" +
                "box-shadow: 2px 2px 12px gray;" +
                "width: fit-content;" +
                "}" +
                ".greeting {" +
                "font-weight: 600;" +
                "font-style: italic;" +
                "}" +
                ".account-info {" +
                "font-weight: 600;" +
                "}" +
                ".footer {" +
                "font-style: italic;" +
                "color: red;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<p class=\"greeting\">XIN CHÀO BẠN, EMAIL ĐƯỢC GỬI TỪ CHUỖI TRUNG TÂM NGOẠI NGỮ</p>" +
                "<ul style=\"list-style-type: none; padding-left: 20px;\">" +
                "<li>Tài khoản: <span class=\"account-info\">" + username + "</span></li>" +
                "<p>" + message + "</p>" +
                "</ul>" +
                "<p class=\"footer\">Email này được tạo tự động - vui lòng không trả lời! Cám ơn.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
