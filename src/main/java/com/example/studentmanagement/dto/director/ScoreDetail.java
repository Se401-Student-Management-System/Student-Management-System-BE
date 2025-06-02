package com.example.studentmanagement.dto.director;

public class ScoreDetail {
    private Float score15m1;
    private Float score15m2;
    private Float score1h1;
    private Float score1h2;
    private Float finalScore;
    private Double averageSubjectScore;
    private Double yearlyAverageSubjectScore;

    // Getters và Setters
    public Float getScore15m1() { return score15m1; }
    public void setScore15m1(Float score15m1) { this.score15m1 = score15m1; }
    public Float getScore15m2() { return score15m2; }
    public void setScore15m2(Float score15m2) { this.score15m2 = score15m2; }
    public Float getScore1h1() { return score1h1; }
    public void setScore1h1(Float score1h1) { this.score1h1 = score1h1; }
    public Float getScore1h2() { return score1h2; }
    public void setScore1h2(Float score1h2) { this.score1h2 = score1h2; }
    public Float getFinalScore() { return finalScore; }
    public void setFinalScore(Float finalScore) { this.finalScore = finalScore; }
    public Double getAverageSubjectScore() { return averageSubjectScore; }
    public void setAverageSubjectScore(Double averageSubjectScore) { this.averageSubjectScore = averageSubjectScore; }
    public Double getYearlyAverageSubjectScore() { return yearlyAverageSubjectScore; }
    public void setYearlyAverageSubjectScore(Double yearlyAverageSubjectScore) { this.yearlyAverageSubjectScore = yearlyAverageSubjectScore; }
}