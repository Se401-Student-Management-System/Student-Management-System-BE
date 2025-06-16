package com.example.studentmanagement.dto.teacher;

import lombok.Data;

@Data
public class StudentEvaluationDTO {
    private String studentId;
    private String fullName;
    private String subjectName;
    private Float score15m1;
    private Float score15m2;
    private Float score1h1;
    private Float score1h2;
    private Float finalScore;
    private String comments;
    private Float averageScore;
}