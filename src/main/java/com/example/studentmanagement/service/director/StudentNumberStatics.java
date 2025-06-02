package com.example.studentmanagement.service.director;

import com.example.studentmanagement.repository.StudentClassRepository;
import com.example.studentmanagement.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentNumberStatics {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private double calculatePercentageChange(long currentCount, long previousCount) {
        if (previousCount == 0) {
            return currentCount > 0 ? 100.0 : 0.0;
        }
        return ((double) (currentCount - previousCount) / previousCount) * 100.0;
    }

    public String getPreviousAcademicYear(String academicYear) {
        if (academicYear == null || !academicYear.matches("\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException("Academic year must be in format YYYY-YYYY");
        }
        String[] years = academicYear.split("-");
        int startYear = Integer.parseInt(years[0]) - 1;
        int endYear = Integer.parseInt(years[1]) - 1;
        return startYear + "-" + endYear;
    }

    public long getEnrolledStudents(String academicYear) {
        try {
            String sql = "SELECT COUNT(DISTINCT s.id) FROM student s " +
                         "JOIN student_class sc ON s.id = sc.student_id " +
                         "WHERE sc.academic_year = :year " +
                         "AND s.id NOT IN (" +
                         "    SELECT s2.id FROM student s2 " +
                         "    JOIN student_class sc2 ON s2.id = sc2.student_id " +
                         "    WHERE sc2.academic_year < :year" +
                         ")";
            var query = entityManager.createNativeQuery(sql)
                    .setParameter("year", academicYear);
            return ((Number) query.getSingleResult()).longValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Object> getEnrolledStatistics(String academicYear) {
        long currentEnrolled = getEnrolledStudents(academicYear);
        long previousEnrolled = getEnrolledStudents(getPreviousAcademicYear(academicYear));
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", currentEnrolled);
        stats.put("change", Math.round(calculatePercentageChange(currentEnrolled, previousEnrolled) * 100.0) / 100.0);
        return stats;
    }
    // Hàm này có thể thay đổi theo status của học sinh
    // Nếu status là "ACTIVE" thì có thể dùng hàm này
    public long getActiveStudents(int grade, String academicYear) {
        try {
            String sql = "SELECT COUNT(DISTINCT s.id) FROM student s " +
                         "JOIN student_class sc ON s.id = sc.student_id " +
                         "WHERE s.status = 'ACTIVE' AND sc.academic_year = :year";
            if (grade > 0) {
                sql += " AND sc.class_id IN (SELECT id FROM class WHERE class_name LIKE :gradePrefix)";
            }
            var query = entityManager.createNativeQuery(sql)
                    .setParameter("year", academicYear);
            if (grade > 0) {
                query.setParameter("gradePrefix", grade + "_%");
            }
            return ((Number) query.getSingleResult()).longValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Object> getActiveStatistics(int grade, String academicYear) {
        long currentActive = getActiveStudents(grade, academicYear);
        long previousActive = getActiveStudents(grade, getPreviousAcademicYear(academicYear));
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", currentActive);
        stats.put("change", Math.round(calculatePercentageChange(currentActive, previousActive) * 100.0) / 100.0);
        return stats;
    }

    public long getWarningStudents(int grade, String academicYear) {
        try {
            String sql = "SELECT COUNT(DISTINCT b.student_id) FROM behavior b " +
                         "JOIN student_class sc ON b.student_id = sc.student_id " +
                         "WHERE b.academic_year = :year AND b.behavior_score < 50";
            if (grade > 0) {
                sql += " AND sc.class_id IN (SELECT id FROM class WHERE class_name LIKE :gradePrefix)";
            }
            var query = entityManager.createNativeQuery(sql)
                    .setParameter("year", academicYear);
            if (grade > 0) {
                query.setParameter("gradePrefix", grade + "_%");
            }
            return ((Number) query.getSingleResult()).longValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Object> getWarnedStatistics(int grade, String academicYear) {
        long currentWarned = getWarningStudents(grade, academicYear);
        long previousWarned = getWarningStudents(grade, getPreviousAcademicYear(academicYear));
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", currentWarned);
        stats.put("change", Math.round(calculatePercentageChange(currentWarned, previousWarned) * 100.0) / 100.0);
        return stats;
    }

    public long getPendingStudents(int grade, String academicYear) {
        return 0; // Placeholder
    }

    public long getInactiveStudents(int grade, String academicYear) {
        return 0; // Placeholder
    }

    public Map<String, String> getEnrolledStudentHistory() {
        try {
            String sql = "SELECT sc.academic_year, COUNT(DISTINCT s.id) AS count FROM student s " +
                         "JOIN student_class sc ON s.id = sc.student_id " +
                         "WHERE s.id NOT IN (" +
                         "    SELECT s2.id FROM student s2 " +
                         "    JOIN student_class sc2 ON s2.id = sc2.student_id " +
                         "    WHERE sc2.academic_year < sc.academic_year" +
                         ") " +
                         "GROUP BY sc.academic_year";
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
            return results.stream()
                    .collect(Collectors.toMap(
                            row -> row[0].toString(),
                            row -> row[1].toString()
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Map<String, String> getWarnedStudentHistory() {
        try {
            String sql = "SELECT b.academic_year, COUNT(DISTINCT b.student_id) AS count FROM behavior b " +
                         "WHERE b.behavior_score < 50 " +
                         "GROUP BY b.academic_year";
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
            return results.stream()
                    .collect(Collectors.toMap(
                            row -> row[0].toString(),
                            row -> row[1].toString()
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}