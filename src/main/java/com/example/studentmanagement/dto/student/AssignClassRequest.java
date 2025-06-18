package com.example.studentmanagement.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignClassRequest {
    @NotBlank(message = "StudentId cannot be empty")
    private String studentId;

    @NotBlank(message = "Classname cannot be empty")
    private String className;

    @NotBlank(message = "Academic year cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "Academic year must be in YYYY-YYYY format")
    private String academicYear;
}
