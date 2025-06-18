package com.example.studentmanagement.dto.behavior;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorEvaluationResponse {
    private String studentId;
    private String studentName;
    private String academicYear;
    private Integer semester;
    private Integer evaluatedBehaviorScore; // Điểm hạnh kiểm vừa được đánh giá
    private String behaviorStatus;         // Trạng thái hạnh kiểm (Good, Fair, Average, Poor)
    private String studentCurrentStatus;   // Trạng thái học sinh sau khi đánh giá (ACTIVE, WARNING, INACTIVE...)
    private String message;                // Thông báo kết quả (ví dụ: "Đã đánh giá...", "Đã chuyển trạng thái Warning...")
}
