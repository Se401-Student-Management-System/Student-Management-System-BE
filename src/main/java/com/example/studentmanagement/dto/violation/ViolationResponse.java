package com.example.studentmanagement.dto.violation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationResponse {
    private String message;          // Thông báo tổng quan
    private String studentId;
    private String studentName;
    private String violationName;
    private Double deductedPoints;   // Số điểm bị trừ
    private Integer newBehaviorScore; // Điểm hạnh kiểm mới
    private String newStudentStatus; // Trạng thái sinh viên mới (nếu có thay đổi)
    private boolean emailSent;       // Có gửi email không
    private String severity;         // Mức độ nghiêm trọng của vi phạm
}