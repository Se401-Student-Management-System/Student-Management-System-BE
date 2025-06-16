package com.example.studentmanagement.designpattern.decorator;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationDecorator extends EvaluationDecorator {

    private static final Logger logger = LoggerFactory.getLogger(NotificationDecorator.class);
    private final JavaMailSender mailSender;
    private final List<Score> processedScores = new ArrayList<>();
    private Teacher currentTeacher;

    @Autowired
    public NotificationDecorator(EvaluationComponent component, JavaMailSender mailSender) {
        super(component);
        this.mailSender = mailSender;
    }

    @Override
    public void addComment(Score score, Teacher teacher, String comment) {
        // Lưu thông tin để gửi email cho giáo viên sau khi xử lý tất cả
        synchronized (processedScores) {
            processedScores.add(score);
            currentTeacher = teacher;
        }

        // Gọi phương thức của component để thêm nhận xét
        super.addComment(score, teacher, comment);

        // Gửi email đến học sinh ngay lập tức
        sendStudentEmail(score, teacher);
    }

    public void sendTeacherEmail() {
        synchronized (processedScores) {
            if (processedScores.isEmpty() || currentTeacher == null) {
                return;
            }

            String teacherEmail = currentTeacher.getAccount().getEmail();
            int count = processedScores.size();
            int semester = processedScores.get(0).getSemester();
            String academicYear = processedScores.get(0).getAcademicYear();
            String subject = String.format(
                "Xác Nhận Thêm Đánh Giá Cho %d Học Sinh - Học Kỳ %d Năm Học %s",
                count, semester, academicYear
            );
            String messageText = String.format(
                "Kính gửi Giáo viên %s,\n\n" +
                "Hệ thống quản lý học sinh xin xác nhận: Quý thầy/cô đã thêm đánh giá thành công cho %d học sinh trong học kỳ %d, năm học %s.\n\n" +
                "Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ bộ phận hỗ trợ qua email.\n\n" +
                "Trân trọng,\n" +
                "Hệ Thống Quản Lý Học Sinh\n",
                currentTeacher.getAccount().getFullName(),
                count,
                semester,
                academicYear
            );

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(teacherEmail);
            message.setSubject(subject);
            message.setText(messageText);

            try {
                mailSender.send(message);
                logger.info("Gửi email xác nhận đến giáo viên {} thành công: {} học sinh", teacherEmail, count);
            } catch (Exception e) {
                logger.error("Lỗi khi gửi email đến giáo viên {}: {}", teacherEmail, e.getMessage());
            }

            // Xóa danh sách sau khi gửi
            processedScores.clear();
            currentTeacher = null;
        }
    }

    private void sendStudentEmail(Score score, Teacher teacher) {
        String studentEmail = score.getStudent().getAccount().getEmail();
        String subject = String.format(
            "Thông Báo Đánh giá Kết quả Học tập Từ Giáo Viên - Môn %s Học Kỳ %d Năm Học %s",
            score.getSubject().getSubjectName(), score.getSemester(), score.getAcademicYear()
        );
        String messageText = String.format(
            "Kính gửi %s,\n\n" +
            "Hệ thống quản lý học sinh xin thông báo: Giáo viên %s đã thêm đánh giá mới cho bạn trong môn %s của học kỳ %d, năm học %s. Chi tiết như sau:\n\n" +
            "- Môn học: %s\n" +
            "- Học kỳ: %d\n" +
            "- Năm học: %s\n" +
            "- Nhận xét: %s\n\n" +
            "Vui lòng đăng nhập vào hệ thống quản lý học sinh để xem thêm chi tiết hoặc liên hệ giáo viên nếu có thắc mắc.\n\n" +
            "Trân trọng,\n" +
            "Hệ Thống Quản Lý Học Sinh\n",
            score.getStudent().getAccount().getFullName(),
            teacher.getAccount().getFullName(),
            score.getSubject().getSubjectName(),
            score.getSemester(),
            score.getAcademicYear(),
            score.getSubject().getSubjectName(),
            score.getSemester(),
            score.getAcademicYear(),
            score.getComments() != null ? score.getComments() : "Không có nhận xét"
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(studentEmail);
        message.setSubject(subject);
        message.setText(messageText);

        try {
            mailSender.send(message);
            logger.info("Gửi email thông báo đến học sinh {} thành công", studentEmail);
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email đến học sinh {}: {}", studentEmail, e.getMessage());
        }
    }

    // Getter để TeacherService truy cập mailSender
    public JavaMailSender getMailSender() {
        return mailSender;
    }
}