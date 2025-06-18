package com.example.studentmanagement.dto.behavior;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEvalutionRequest {
    @NotBlank(message = "Student ID cannot be empty")
    private String studentId;

    @NotBlank(message = "Academic year cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "Academic year must be in YYYY-YYYY format")
    private String academicYear;

    @NotNull(message = "Semester cannot be null")
    private Integer semester;

    @NotNull(message = "Behavior score cannot be null")
    @Min(value = 0, message = "Behavior score must be at least 0")
    @Max(value = 100, message = "Behavior score must be at most 100")
    private Integer behaviorScore;
}
