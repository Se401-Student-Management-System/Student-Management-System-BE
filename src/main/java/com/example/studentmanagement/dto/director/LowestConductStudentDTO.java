package com.example.studentmanagement.dto.director;

import lombok.Data;

@Data
public class LowestConductStudentDTO {
    private String studentId;
    private String fullName;
    private String className;
    private Integer behaviorScore;
    private String status;
    private Long violationCount;
}