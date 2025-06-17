package com.example.studentmanagement.designpattern.strategy;

import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Behavior;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Violation;
import com.example.studentmanagement.repository.BehaviorRepository;
import com.example.studentmanagement.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("majorViolationStrategy")
public class MajorViolationStrategy implements ViolationHandlingStrategy {
    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private BehaviorRepository behaviorRepository;

    @Override
    public ViolationResponse handleViolation(Student student, Violation violation) {
        String studentFullName = student.getAccount().getFullName();
        String studentId = student.getId();
        String violationTypeName = violation.getViolationType().getViolationName();
        Double deductedPoints = violation.getViolationType().getDeductedPoints();
        Integer newBehaviorScore = null;
        String newStudentStatus = student.getStatus().name();
        boolean emailSent = false;

        System.out.println("[Major] Processing violation for " + studentFullName + ": " + violationTypeName);

        // Logic trừ điểm
        if (deductedPoints != null && deductedPoints > 0) {
            Optional<Behavior> behaviorOptional = behaviorRepository.findByStudentAndAcademicYearAndSemester(
                    student, violation.getAcademicYear(), violation.getSemester()
            );

            Behavior behavior;
            if (behaviorOptional.isPresent()) {
                behavior = behaviorOptional.get();
            } else {
                behavior = new Behavior();
                behavior.setStudent(student);
                behavior.setAcademicYear(violation.getAcademicYear());
                behavior.setSemester(violation.getSemester());
                behavior.setBehaviorScore(100);
                behavior.setStatus("Tốt");
                behaviorRepository.save(behavior);
                System.out.println("[Major] Created new Behavior record for " + studentFullName + ".");
            }

            Integer currentScore = behavior.getBehaviorScore();
            if (currentScore != null) {
                behavior.setBehaviorScore(Math.max(0, currentScore - deductedPoints.intValue()));
                behaviorRepository.save(behavior);
                newBehaviorScore = behavior.getBehaviorScore();
                System.out.println("[Major] Deducted " + deductedPoints + " points. New score: " + newBehaviorScore);
            }
        }

        // Gửi email cảnh báo khẩn cấp
        String studentEmail = student.getAccount().getEmail();
        String adminEmail = "admin@school.com";
        String recipients = studentEmail + "," + adminEmail; // Gửi đến nhiều người
        String subject = "[KHẨN CẤP] Vi phạm nghiêm trọng của học sinh " + studentFullName;
        String customMessage = "Chúng tôi xin thông báo rằng học sinh " + studentFullName + " đã có hành vi vi phạm RẤT NGHIÊM TRỌNG: " + violationTypeName + ". Sự việc này yêu cầu hành động kỷ luật nghiêm khắc từ phía nhà trường.";
        String htmlBody = emailUtil.createWarningEmailBody(
                studentFullName, violationTypeName, violation.getViolationDate(), customMessage
        );
        emailUtil.sendHtmlEmail(recipients, subject, htmlBody);
        emailSent = true;

        // Chuyển trạng thái sinh viên sang "Nghỉ học" (INACTIVE)
        if (student.getStatus() != StudyStatus.INACTIVE) {
            student.performLeave();
            newStudentStatus = student.getStatus().name();
            System.out.println("[Major] Student " + studentId + " status changed to " + newStudentStatus + ".");
        }

        return new ViolationResponse(
                "Vi phạm nghiêm trọng đã được ghi nhận và xử lý.",
                studentId,
                studentFullName,
                violationTypeName,
                deductedPoints,
                newBehaviorScore,
                newStudentStatus,
                emailSent,
                "Major"
        );
    }
}
