package com.example.studentmanagement.dto.teacher;

import lombok.Data;

import java.util.List;

@Data
public class ScoreRequest {
    private List<ScoreDetail> scores;
    private Integer semester;
    private String academicYear;
    private Integer subjectId;

    @Data
    public static class ScoreDetail {
        private String studentId;
        private Float score15m1;
        private Float score15m2;
        private Float score1h1;
        private Float score1h2;
        private Float finalScore;

        // Constructors
        public ScoreDetail() {}

        public ScoreDetail(String studentId, Float score15m1, Float score15m2, Float score1h1, Float score1h2, Float finalScore) {
            this.studentId = studentId;
            this.score15m1 = score15m1;
            this.score15m2 = score15m2;
            this.score1h1 = score1h1;
            this.score1h2 = score1h2;
            this.finalScore = finalScore;
        }
    }
}