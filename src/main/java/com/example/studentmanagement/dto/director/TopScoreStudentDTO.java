package com.example.studentmanagement.dto.director;

import lombok.Data;

@Data
public class TopScoreStudentDTO {
    private String studentId;
    private String fullName;
    private String className;
    private Double averageScore;
}