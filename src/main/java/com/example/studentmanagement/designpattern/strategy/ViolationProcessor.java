package com.example.studentmanagement.designpattern.strategy;

import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Violation;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ViolationProcessor {

    private ViolationHandlingStrategy strategy;

    @Autowired
    private StudentRepository studentRepository;

    public void setStrategy(ViolationHandlingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Thực thi chiến lược hiện tại để xử lý vi phạm.
     * Context không biết đang làm việc với loạic chiến lược nào hay thuật toán được thực thi như thế nào.
     * Thao tác này cần @Transactional vì nó có thể cập nhật Student và Behavior.
     * Trả về ViolationResponse từ strategy.
     */
    @Transactional
    public ViolationResponse processViolation(Student student, Violation violation) {
        if (strategy == null) {
            throw new IllegalStateException("Violation handling strategy has not been set.");
        }
        ViolationResponse response = strategy.handleViolation(student, violation);

        // Lưu Student sau khi chiến lược có thể đã thay đổi trạng thái của nó
        // (Ví dụ: Moderate/MajorViolationStrategy có thể gọi student.performWarn()/performLeave())
        // Việc lưu Student entity là trách nhiệm của Context để đảm bảo tính nhất quán của giao dịch.
        studentRepository.save(student);

        return response;
    }
}