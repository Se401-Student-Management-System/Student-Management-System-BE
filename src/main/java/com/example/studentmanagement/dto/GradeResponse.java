package com.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeResponse {
    private String subjectName;
    private Float score15m1;
    private Float score15m2;
    private Float score1h1;
    private Float score1h2;
    private Float finalScore;
    private Integer semester;
    private String academicYear;
    private String comments;
}