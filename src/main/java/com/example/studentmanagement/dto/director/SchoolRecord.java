package com.example.studentmanagement.dto.director;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Subject;

import java.util.HashMap;
import java.util.Map;

public class SchoolRecord {
    private final Student student;
    private final String studentName;
    private final String className;
    private final Map<Integer, Map<Subject, ScoreDetail>> scoresBySemester;
    private final Map<Integer, Double> averageScoresBySemester;
    private final Map<Subject, Double> yearlyAverageScoresBySubject;
    private final Double yearlyAverageScore;
    private final Double behaviorScore;
    private final String academicPerformance;
    private final String title;
    private final String comment;
    private final int semester;

    public SchoolRecord(Student student, String studentName, String className,
                        Map<Integer, Map<Subject, ScoreDetail>> scoresBySemester,
                        Map<Integer, Double> averageScoresBySemester,
                        Map<Subject, Double> yearlyAverageScoresBySubject,
                        Double yearlyAverageScore, Double behaviorScore,
                        String academicPerformance, String title, String comment, int semester) {
        this.student = student;
        this.studentName = studentName;
        this.className = className;
        // Defensive copy để đảm bảo bất biến
        this.scoresBySemester = new HashMap<>();
        if (scoresBySemester != null) {
            scoresBySemester.forEach((k, v) -> this.scoresBySemester.put(k, new HashMap<>(v)));
        }
        this.averageScoresBySemester = averageScoresBySemester != null ? new HashMap<>(averageScoresBySemester) : new HashMap<>();
        this.yearlyAverageScoresBySubject = yearlyAverageScoresBySubject != null ? new HashMap<>(yearlyAverageScoresBySubject) : new HashMap<>();
        this.yearlyAverageScore = yearlyAverageScore;
        this.behaviorScore = behaviorScore;
        this.academicPerformance = academicPerformance;
        this.title = title;
        this.comment = comment;
        this.semester = semester;
    }

    public Student getStudent() { return student; }
    public String getStudentName() { return studentName; }
    public String getClassName() { return className; }
    public Map<Integer, Map<Subject, ScoreDetail>> getScoresBySemester() { return scoresBySemester; }
    public Map<Integer, Double> getAverageScoresBySemester() { return averageScoresBySemester; }
    public Map<Subject, Double> getYearlyAverageScoresBySubject() { return yearlyAverageScoresBySubject; }
    public Double getYearlyAverageScore() { return yearlyAverageScore; }
    public Double getBehaviorScore() { return behaviorScore; }
    public String getAcademicPerformance() { return academicPerformance; }
    public String getTitle() { return title; }
    public String getComment() { return comment; }
    public int getSemester() { return semester; }

    public Double getAverageScore() {
        return averageScoresBySemester.getOrDefault(semester, null);
    }

    public Map<Subject, ScoreDetail> getScoresForSemester(int semester) {
        return scoresBySemester.getOrDefault(semester, new HashMap<>());
    }
}