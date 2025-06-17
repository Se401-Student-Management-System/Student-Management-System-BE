package com.example.studentmanagement.designpattern.strategy;

import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.model.Behavior;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Violation;
import com.example.studentmanagement.repository.BehaviorRepository;
import com.example.studentmanagement.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Concrete Strategy: Xử lý vi phạm nhẹ.
 * - Hiện popup cảnh báo (in ra console).
 * - Trừ điểm hành kiểm ít (ví dụ: dựa trên deductedPoints của ViolationType).
 * - Không gửi email hoặc thay đổi trạng thái.
 */
@Component("minorViolationStategy")
public class MinorViolationStrategy implements ViolationHandlingStrategy {
    @Autowired
    private BehaviorRepository behaviorRepository;


    @Override
    public ViolationResponse handleViolation(Student student, Violation violation) {
        String studentFullName = student.getAccount().getFullName();
        String studentId = student.getId();
        String violationTypeName = violation.getViolationType().getViolationName();
        Double deductedPoints = violation.getViolationType().getDeductedPoints();
        Integer newBehaviorScore = null;

        System.out.println("[Minor] Processing violation for " + studentFullName + ": " + violationTypeName);

        if (deductedPoints != null && deductedPoints > 0) {
            Optional<Behavior> behaviorOptional = behaviorRepository.findByStudentAndAcademicYearAndSemester(
                    student, violation.getAcademicYear(), violation.getSemester()
            );

            Behavior behavior;
            if (behaviorOptional.isPresent()) {
                behavior = behaviorOptional.get();
            } else {
                // Tạo mới bản ghi Behavior nếu không tồn tại
                behavior = new Behavior();
                behavior.setStudent(student);
                behavior.setAcademicYear(violation.getAcademicYear());
                behavior.setSemester(violation.getSemester());
                behavior.setBehaviorScore(100); // Điểm hạnh kiểm mặc định khi tạo mới
                behavior.setStatus("Tốt"); // Trạng thái mặc định
                behaviorRepository.save(behavior);
                System.out.println("[Minor] Created new Behavior record for " + studentFullName + ".");
            }

            Integer currentScore = behavior.getBehaviorScore();
            if (currentScore != null) {
                behavior.setBehaviorScore(Math.max(0, currentScore - deductedPoints.intValue()));
                behaviorRepository.save(behavior);
                newBehaviorScore = behavior.getBehaviorScore();
                System.out.println("[Minor] Deducted " + deductedPoints + " points. New score: " + newBehaviorScore);
            }
        }

        return new ViolationResponse(
                "Vi phạm nhẹ đã được ghi nhận.",
                studentId,
                studentFullName,
                violationTypeName,
                deductedPoints,
                newBehaviorScore,
                student.getStatus().name(), // Trạng thái hiện tại của học sinh
                false, // Không gửi email
                "Minor"
        );
    }
}
