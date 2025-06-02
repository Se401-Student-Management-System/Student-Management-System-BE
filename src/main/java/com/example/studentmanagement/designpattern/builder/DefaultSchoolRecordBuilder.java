package com.example.studentmanagement.designpattern.builder;

import com.example.studentmanagement.dto.director.ScoreDetail;
import com.example.studentmanagement.dto.director.SchoolRecord;
import com.example.studentmanagement.model.*;
import com.example.studentmanagement.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DefaultSchoolRecordBuilder implements SchoolRecordBuilder {
    private Student student;
    private String studentName;
    private String className;
    private final Map<Integer, Map<Subject, ScoreDetail>> scoresBySemester = new HashMap<>();
    private final Map<Integer, Double> averageScoresBySemester = new HashMap<>();
    private final Map<Subject, Double> yearlyAverageScoresBySubject = new HashMap<>();
    private Double yearlyAverageScore;
    private Double behaviorScore;
    private String academicPerformance;
    private String title;
    private String comment;
    private int semester;

    private final StudentRepository studentRepository;
    private final StudentClassRepository studentClassRepository;
    private final BehaviorRepository behaviorRepository;
    private final ScoreRepository scoreRepository;
    private final TitleRepository titleRepository;
    private final AssignmentRepository assignmentRepository;

    public DefaultSchoolRecordBuilder(StudentRepository studentRepository, StudentClassRepository studentClassRepository,
                                      BehaviorRepository behaviorRepository, ScoreRepository scoreRepository,
                                      TitleRepository titleRepository, AssignmentRepository assignmentRepository) {
        this.studentRepository = studentRepository;
        this.studentClassRepository = studentClassRepository;
        this.behaviorRepository = behaviorRepository;
        this.scoreRepository = scoreRepository;
        this.titleRepository = titleRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public SchoolRecordBuilder setStudentInfo(Student student, String studentName, String className) {
        this.student = student;
        this.studentName = studentName;
        this.className = className;
        return this;
    }

    @Override
    public SchoolRecordBuilder setScores(Student student, int semester, String academicYear) {
        this.semester = semester;
        List<Score> scores = scoreRepository.findByStudentAndSemesterAndAcademicYear(student, semester, academicYear);
        Map<Subject, ScoreDetail> scoreDetails = new HashMap<>();

        for (Score score : scores) {
            ScoreDetail detail = new ScoreDetail();
            detail.setScore15m1(score.getScore15m1());
            detail.setScore15m2(score.getScore15m2());
            detail.setScore1h1(score.getScore1h1());
            detail.setScore1h2(score.getScore1h2());
            detail.setFinalScore(score.getFinalScore());
            scoreDetails.put(score.getSubject(), detail);
        }

        scoresBySemester.put(semester, scoreDetails);
        return this;
    }

    @Override
    public SchoolRecordBuilder calculateAverageScore(int semester, String academicYear) {
        Map<Subject, ScoreDetail> scores = scoresBySemester.getOrDefault(semester, new HashMap<>());
        if (scores.isEmpty()) {
            averageScoresBySemester.put(semester, null);
            return this;
        }

        StudentClass studentClass = studentClassRepository.findByStudentAndAcademicYear(student, academicYear)
                .orElseThrow(() -> new RuntimeException("Lớp học của học sinh không tồn tại"));
        List<Assignment> assignments = assignmentRepository.findByClazzAndSemesterAndAcademicYear(studentClass.getClazz(), semester, academicYear);
        int totalSubjects = assignments.size();

        BigDecimal totalScore = BigDecimal.ZERO;
        int validSubjectCount = 0;

        for (Map.Entry<Subject, ScoreDetail> entry : scores.entrySet()) {
            ScoreDetail detail = entry.getValue();
            Double avgSubject = calculateSubjectAverage(detail);
            detail.setAverageSubjectScore(avgSubject);

            if (avgSubject != null) {
                totalScore = totalScore.add(BigDecimal.valueOf(avgSubject));
                validSubjectCount++;
            }
        }

        if (validSubjectCount == totalSubjects && totalSubjects > 0) {
            BigDecimal average = totalScore.divide(BigDecimal.valueOf(totalSubjects), 1, RoundingMode.HALF_UP);
            averageScoresBySemester.put(semester, average.doubleValue());
        } else {
            averageScoresBySemester.put(semester, null);
        }
        return this;
    }

    private Double calculateSubjectAverage(ScoreDetail detail) {
        if (detail.getScore15m1() == null || detail.getScore15m2() == null ||
            detail.getScore1h1() == null || detail.getScore1h2() == null || detail.getFinalScore() == null) {
            return null;
        }
        BigDecimal score15mSum = BigDecimal.valueOf(detail.getScore15m1() + detail.getScore15m2());
        BigDecimal score1hSum = BigDecimal.valueOf(detail.getScore1h1() + detail.getScore1h2()).multiply(BigDecimal.valueOf(2));
        BigDecimal finalScoreWeighted = BigDecimal.valueOf(detail.getFinalScore()).multiply(BigDecimal.valueOf(3));
        BigDecimal total = score15mSum.add(score1hSum).add(finalScoreWeighted);
        return total.divide(BigDecimal.valueOf(9), 1, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public SchoolRecordBuilder calculateYearlyAveragePerSubject(String academicYear) {
        Map<Subject, ScoreDetail> scoresSem1 = scoresBySemester.getOrDefault(1, new HashMap<>());
        Map<Subject, ScoreDetail> scoresSem2 = scoresBySemester.getOrDefault(2, new HashMap<>());

        if (scoresSem1.isEmpty() || scoresSem2.isEmpty()) {
            return this;
        }

        for (Subject subject : scoresSem1.keySet()) {
            ScoreDetail detailSem1 = scoresSem1.get(subject);
            ScoreDetail detailSem2 = scoresSem2.get(subject);
            if (detailSem1 != null && detailSem2 != null) {
                Double avgSem1 = detailSem1.getAverageSubjectScore();
                Double avgSem2 = detailSem2.getAverageSubjectScore();
                if (avgSem1 != null && avgSem2 != null) {
                    BigDecimal sem1 = BigDecimal.valueOf(avgSem1);
                    BigDecimal sem2Weighted = BigDecimal.valueOf(avgSem2).multiply(BigDecimal.valueOf(2));
                    BigDecimal total = sem1.add(sem2Weighted);
                    BigDecimal yearlyAvg = total.divide(BigDecimal.valueOf(3), 1, RoundingMode.HALF_UP);
                    detailSem2.setYearlyAverageSubjectScore(yearlyAvg.doubleValue());
                    yearlyAverageScoresBySubject.put(subject, yearlyAvg.doubleValue());
                }
            }
        }

        if (!yearlyAverageScoresBySubject.isEmpty()) {
            BigDecimal totalYearlyScore = BigDecimal.ZERO;
            int validSubjectCount = 0;
            for (Double score : yearlyAverageScoresBySubject.values()) {
                if (score != null) {
                    totalYearlyScore = totalYearlyScore.add(BigDecimal.valueOf(score));
                    validSubjectCount++;
                }
            }
            if (validSubjectCount > 0) {
                BigDecimal yearlyAverage = totalYearlyScore.divide(BigDecimal.valueOf(validSubjectCount), 1, RoundingMode.HALF_UP);
                this.yearlyAverageScore = yearlyAverage.doubleValue();
            }
        }

        return this;
    }

    @Override
    public SchoolRecordBuilder calculateYearlyAverageScore(String academicYear) {
        setScores(student, 1, academicYear);
        calculateAverageScore(1, academicYear);
        Double semester1Average = averageScoresBySemester.get(1);

        setScores(student, 2, academicYear);
        calculateAverageScore(2, academicYear);
        Double semester2Average = averageScoresBySemester.get(2);

        if (semester1Average != null && semester2Average != null) {
            BigDecimal sem1 = BigDecimal.valueOf(semester1Average);
            BigDecimal sem2Weighted = BigDecimal.valueOf(semester2Average).multiply(BigDecimal.valueOf(2));
            BigDecimal total = sem1.add(sem2Weighted);
            BigDecimal yearlyAverage = total.divide(BigDecimal.valueOf(3), 1, RoundingMode.HALF_UP);
            this.yearlyAverageScore = yearlyAverage.doubleValue();
            averageScoresBySemester.put(0, yearlyAverage.doubleValue());
        } else {
            this.yearlyAverageScore = null;
            averageScoresBySemester.put(0, null);
        }

        return this;
    }

    @Override
    public SchoolRecordBuilder setBehaviorScore(Student student, int semester, String academicYear) {
        Behavior behavior = behaviorRepository.findByStudentAndSemesterAndAcademicYear(student, semester, academicYear)
                .orElseThrow(() -> new RuntimeException("Hạnh kiểm không tồn tại"));
        this.behaviorScore = (double) behavior.getBehaviorScore();
        return this;
    }

    @Override
    public SchoolRecordBuilder setAcademicPerformance() {
        Double avgScore = averageScoresBySemester.getOrDefault(semester, null);
        if (avgScore == null) {
            this.academicPerformance = "Chưa xếp loại";
            return this;
        }
        if (avgScore >= 8.0) {
            this.academicPerformance = "Giỏi";
        } else if (avgScore >= 6.5) {
            this.academicPerformance = "Khá";
        } else if (avgScore >= 5.0) {
            this.academicPerformance = "Trung bình";
        } else {
            this.academicPerformance = "Yếu";
        }
        return this;
    }

    @Override
    public SchoolRecordBuilder setTitle() {
        Double avgScore = averageScoresBySemester.getOrDefault(semester, null);
        if (avgScore == null || behaviorScore == null) {
            this.title = null;
            return this;
        }

        Title title = titleRepository.findByMinAvgScoreLessThanEqualAndMinBehaviorScoreLessThanEqual(avgScore, behaviorScore)
                .stream()
                .max(Comparator.comparing(Title::getMinAvgScore))
                .orElse(null);
        this.title = title != null ? title.getTitleName() : null;
        return this;
    }

    @Override
    public SchoolRecordBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public SchoolRecordBuilder setAverageScore(int semester, Double averageScore) {
        averageScoresBySemester.put(semester, averageScore);
        return this;
    }

    @Override
    public SchoolRecord build() {
        return new SchoolRecord(
                student,
                studentName,
                className,
                scoresBySemester,
                averageScoresBySemester,
                yearlyAverageScoresBySubject,
                yearlyAverageScore,
                behaviorScore,
                academicPerformance,
                title,
                comment,
                semester
        );
    }

    @Override
    public StudentRepository getStudentRepository() { return studentRepository; }
    @Override
    public StudentClassRepository getStudentClassRepository() { return studentClassRepository; }
    @Override
    public BehaviorRepository getBehaviorRepository() { return behaviorRepository; }
    @Override
    public ScoreRepository getScoreRepository() { return scoreRepository; }
    @Override
    public TitleRepository getTitleRepository() { return titleRepository; }
    @Override
    public AssignmentRepository getAssignmentRepository() { return assignmentRepository; }
}