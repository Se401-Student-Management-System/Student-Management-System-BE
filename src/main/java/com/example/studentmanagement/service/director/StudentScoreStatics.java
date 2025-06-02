package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.TopScoreStudentDTO;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.AssignmentRepository;
import com.example.studentmanagement.repository.ScoreRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentScoreStatics {
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    private double calculatePercentageChange(long currentCount, long previousCount) {
        if (previousCount == 0) {
            return currentCount > 0 ? 100.0 : 0.0;
        }
        return ((double) (currentCount - previousCount) / previousCount) * 100.0;
    }

private Double calculateAverageScore(List<Score> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }

        BigDecimal total = BigDecimal.ZERO;
        int validSubjects = 0;

        for (Score score : scores) {
            if (score.getScore15m1() == null || score.getScore15m2() == null ||
                score.getScore1h1() == null || score.getScore1h2() == null || 
                score.getFinalScore() == null) {
                return null;
            }

            try {
                BigDecimal score15m1 = BigDecimal.valueOf(score.getScore15m1());
                BigDecimal score15m2 = BigDecimal.valueOf(score.getScore15m2());
                BigDecimal score1h1 = BigDecimal.valueOf(score.getScore1h1());
                BigDecimal score1h2 = BigDecimal.valueOf(score.getScore1h2());
                BigDecimal finalScore = BigDecimal.valueOf(score.getFinalScore());

                if (score15m1.compareTo(BigDecimal.ZERO) < 0 || score15m2.compareTo(BigDecimal.ZERO) < 0 ||
                    score1h1.compareTo(BigDecimal.ZERO) < 0 || score1h2.compareTo(BigDecimal.ZERO) < 0 ||
                    finalScore.compareTo(BigDecimal.ZERO) < 0) {
                    return null;
                }

                // Tính ĐTB môn: (score_15m_1 + score_15m_2 + 2 * (score_1h_1 + score_1h_2) + 3 * final_score) / 9
                BigDecimal sum1h = score1h1.add(score1h2).multiply(BigDecimal.valueOf(2)); // 2 * (score_1h_1 + score_1h_2)
                BigDecimal finalWeighted = finalScore.multiply(BigDecimal.valueOf(3)); // 3 * final_score
                BigDecimal subjectSum = score15m1.add(score15m2).add(sum1h).add(finalWeighted);
                BigDecimal subjectAvg = subjectSum.divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP); // Làm tròn 2 chữ số

                total = total.add(subjectAvg);
                validSubjects++;
            } catch (ArithmeticException e) {
                return null;
            }
        }
        if (validSubjects == 0) {
            return null;
        }
        BigDecimal avg = total.divide(BigDecimal.valueOf(validSubjects), 2, RoundingMode.HALF_UP)
                             .setScale(1, RoundingMode.HALF_UP);
                             
        return avg.doubleValue();
    }

    private int countByPerformance(int grade, int semester, String academicYear, double min, double max) {
        String gradePrefix = String.valueOf(grade);
        List<Student> students = studentRepository.findStudentsByGradeAndYear(gradePrefix, academicYear);
        int count = 0;
        for (Student student : students) {
            List<Score> scores = scoreRepository.findScoresByStudentAndSemester(student.getId(), semester, academicYear);
            Double avg = calculateAverageScore(scores);
            if (avg != null && avg >= min && avg < max) count++;
        }
        return count;
    }

    private String getPreviousAcademicYear(String academicYear) {
        String[] years = academicYear.split("-");
        int startYear = Integer.parseInt(years[0]) - 1;
        int endYear = Integer.parseInt(years[1]) - 1;
        return startYear + "-" + endYear;
    }

    public Map<String, Object> getGoodGradeStatistics(int grade, int semester, String academicYear) {
        Map<String, Object> stats = new HashMap<>();
        int currentCount = countByPerformance(grade, semester, academicYear, 8.0, Double.MAX_VALUE);
        int previousCount = countByPerformance(grade, semester, getPreviousAcademicYear(academicYear), 8.0, Double.MAX_VALUE);
        stats.put("excellentCount", currentCount);
        stats.put("excellentChange", Math.round(calculatePercentageChange(currentCount, previousCount) * 100.0) / 100.0);
        return stats;
    }

    public Map<String, Object> getNormalGradeStatistics(int grade, int semester, String academicYear) {
        Map<String, Object> stats = new HashMap<>();
        int currentCount = countByPerformance(grade, semester, academicYear, 6.5, 8.0);
        int previousCount = countByPerformance(grade, semester, getPreviousAcademicYear(academicYear), 6.5, 8.0);
        stats.put("goodCount", currentCount);
        stats.put("goodChange", Math.round(calculatePercentageChange(currentCount, previousCount) * 100.0) / 100.0);
        return stats;
    }

    public Map<String, Object> getMediumGradeStatistics(int grade, int semester, String academicYear) {
        Map<String, Object> stats = new HashMap<>();
        int currentCount = countByPerformance(grade, semester, academicYear, 5.0, 6.5);
        int previousCount = countByPerformance(grade, semester, getPreviousAcademicYear(academicYear), 5.0, 6.5);
        stats.put("averageCount", currentCount);
        stats.put("averageChange", Math.round(calculatePercentageChange(currentCount, previousCount) * 100.0) / 100.0);
        return stats;
    }

    public Map<String, Object> getWeakGradeStatistics(int grade, int semester, String academicYear) {
        Map<String, Object> stats = new HashMap<>();
        int currentCount = countByPerformance(grade, semester, academicYear, 0.0, 5.0);
        int previousCount = countByPerformance(grade, semester, getPreviousAcademicYear(academicYear), 0.0, 5.0);
        stats.put("weakCount", currentCount);
        stats.put("weakChange", Math.round(calculatePercentageChange(currentCount, previousCount) * 100.0) / 100.0);
        return stats;
    }

    public List<TopScoreStudentDTO> getTopTenStudents(int grade, int semester, String academicYear) {
        String gradePrefix = String.valueOf(grade);
        List<Student> students = studentRepository.findStudentsByGradeAndYear(gradePrefix, academicYear);
        Map<String, Double> studentAverages = new HashMap<>();
        for (Student student : students) {
            List<Score> scores = scoreRepository.findScoresByStudentAndSemester(student.getId(), semester, academicYear);
            Double avg = calculateAverageScore(scores);
            if (avg != null) {
                studentAverages.put(student.getId(), avg);
            }
        }
        return studentAverages.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Student student = students.stream().filter(s -> s.getId().equals(entry.getKey())).findFirst().get();
                    TopScoreStudentDTO dto = new TopScoreStudentDTO();
                    dto.setStudentId(student.getId());
                    dto.setFullName(student.getAccount().getFullName());
                    String className = studentClassRepository.findByStudentIdAndAcademicYear(student.getId(), academicYear)
                            .map(sc -> sc.getClazz().getClassName())
                            .orElse("Unknown");
                    dto.setClassName(className);
                    dto.setAverageScore(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}