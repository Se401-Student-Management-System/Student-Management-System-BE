package com.example.studentmanagement.dto.violation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationRequest {
    @NotBlank(message = "Student ID cannot be empty")
    private String studentId;

    // Thay thế @NotNull private Integer classId;
    @NotBlank(message = "Class Name cannot be empty")
    private String className;

    private String supervisorId;

    @NotNull(message = "Violation Type ID cannot be null")
    private Integer violationTypeId;

    @NotBlank(message = "Academic year cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "Academic year must be in YYYY-YYYY format")
    private String academicYear;

    @NotNull(message = "Semester cannot be null")
    private Integer semester;

    @PastOrPresent(message = "Violation date cannot be in the future")
    private LocalDate violationDate;
}
