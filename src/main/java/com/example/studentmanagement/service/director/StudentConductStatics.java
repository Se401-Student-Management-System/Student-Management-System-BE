package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.ConductStatisticDTO;
import com.example.studentmanagement.dto.director.LowestConductStudentDTO;
import com.example.studentmanagement.dto.supervisor.StudentBehaviorSummaryDTO;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.BehaviorRepository;
import com.example.studentmanagement.repository.StudentClassRepository;

import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.ViolationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentConductStatics {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private BehaviorRepository behaviorRepository;

    @Autowired
    private ViolationRepository violationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Getter and Setter for studentRepository
    public StudentRepository getStudentRepository() {
        return studentRepository;
    }

    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Getter and Setter for studentClassRepository
    public StudentClassRepository getStudentClassRepository() {
        return studentClassRepository;
    }

    public void setStudentClassRepository(StudentClassRepository studentClassRepository) {
        this.studentClassRepository = studentClassRepository;
    }

    // Getter and Setter for behaviorRepository
    public BehaviorRepository getBehaviorRepository() {
        return behaviorRepository;
    }

    public void setBehaviorRepository(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }

    // Getter and Setter for violationRepository
    public ViolationRepository getViolationRepository() {
        return violationRepository;
    }

    public void setViolationRepository(ViolationRepository violationRepository) {
        this.violationRepository = violationRepository;
    }

    // Getter and Setter for entityManager
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private String getConductRank(Integer behaviorScore) {
        if (behaviorScore == null) {
            return "Yếu";
        }
        if (behaviorScore >= 90) {
            return "Tốt";
        }
        if (behaviorScore >= 70) {
            return "Khá";
        }
        if (behaviorScore >= 50) {
            return "Trung bình";
        }
        return "Yếu";
    }

    public ConductStatisticDTO countConductRank(int grade, int semester, String academicYear) {
        try {
            String sql = "SELECT b.behavior_score "
                    + "FROM behavior b "
                    + "JOIN student_class sc ON b.student_id = sc.student_id "
                    + "AND sc.academic_year = :year "
                    + "JOIN class c ON sc.class_id = c.id "
                    + "WHERE c.class_name LIKE :gradePrefix "
                    + "AND b.semester = :semester "
                    + "AND b.academic_year = :year";
            String gradePrefix = grade + "_%";
            @SuppressWarnings("unchecked")
            List<Object> results = entityManager.createNativeQuery(sql)
                    .setParameter("year", academicYear)
                    .setParameter("gradePrefix", gradePrefix)
                    .setParameter("semester", semester)
                    .getResultList();

            ConductStatisticDTO stats = new ConductStatisticDTO();
            if (results.isEmpty()) {
                stats.setGoodCount(0);
                stats.setFairCount(0);
                stats.setAverageCount(0);
                stats.setPoorCount(0);
                return stats;
            }

            Map<String, Integer> counts = new HashMap<>();
            counts.put("Tốt", 0);
            counts.put("Khá", 0);
            counts.put("Trung bình", 0);
            counts.put("Yếu", 0);

            for (Object result : results) {
                Integer score = result != null ? ((Number) result).intValue() : null;
                String rank = getConductRank(score);
                counts.put(rank, counts.get(rank) + 1);
            }

            stats.setGoodCount(counts.get("Tốt"));
            stats.setFairCount(counts.get("Khá"));
            stats.setAverageCount(counts.get("Trung bình"));
            stats.setPoorCount(counts.get("Yếu"));

            return stats;
        } catch (Exception e) {
            return new ConductStatisticDTO();
        }
    }

    public List<LowestConductStudentDTO> getLowestConductStudents(int semester, String academicYear) {
        try {
            String sql = "SELECT s.id, a.full_name, c.class_name, b.behavior_score, b.status, "
                    + "COUNT(v.id) AS violation_count "
                    + "FROM student s "
                    + "JOIN account a ON s.account_id = a.id "
                    + "JOIN student_class sc ON s.id = sc.student_id "
                    + "AND sc.academic_year = :year "
                    + "JOIN class c ON sc.class_id = c.id "
                    + "JOIN behavior b ON s.id = b.student_id "
                    + "AND b.semester = :semester AND b.academic_year = :year "
                    + "LEFT JOIN violation v ON s.id = v.student_id "
                    + "AND v.semester = :semester AND v.academic_year = :year "
                    + "GROUP BY s.id, a.full_name, c.class_name, b.behavior_score, b.status "
                    + "ORDER BY b.behavior_score ASC, violation_count DESC, c.class_name ASC "
                    + "LIMIT 20";
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(sql)
                    .setParameter("year", academicYear)
                    .setParameter("semester", semester)
                    .getResultList();

            List<LowestConductStudentDTO> students = new ArrayList<>();
            for (Object[] row : results) {
                LowestConductStudentDTO dto = new LowestConductStudentDTO();
                dto.setStudentId((String) row[0]);
                dto.setFullName((String) row[1]);
                dto.setClassName((String) row[2]);
                dto.setBehaviorScore(row[3] != null ? ((Number) row[3]).intValue() : null);
                dto.setStatus((String) row[4]);
                dto.setViolationCount(((Number) row[5]).longValue());
                students.add(dto);
            }

            return students;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getConductOverview(int grade, int semester, String academicYear) {
        Map<String, Object> result = new HashMap<>();
        // Ví dụ: lấy thống kê tổng hợp cho toàn khối 10
        ConductStatisticDTO statistic = countConductRank(grade, semester, academicYear);
        List<LowestConductStudentDTO> lowestList = getLowestConductStudents(semester, academicYear);
        result.put("statistic", statistic);
        result.put("lowestList", lowestList);
        return result;
    }

    public List<StudentBehaviorSummaryDTO> getStudentBehaviorSummary(int grade, int semester, String academicYear) {
        String sql = "SELECT s.id, a.full_name, c.class_name, SUM(b.behavior_score) as total_behavior_score, b.status "
                + "FROM student s "
                + "JOIN account a ON s.account_id = a.id "
                + "JOIN student_class sc ON s.id = sc.student_id AND sc.academic_year = :year "
                + "JOIN class c ON sc.class_id = c.id "
                + "JOIN behavior b ON s.id = b.student_id AND b.semester = :semester AND b.academic_year = :year "
                + "WHERE c.class_name LIKE :gradePrefix "
                + "GROUP BY s.id, a.full_name, c.class_name, b.status "
                + "ORDER BY total_behavior_score DESC";
        String gradePrefix = grade + "%";
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("year", academicYear)
                .setParameter("semester", semester)
                .setParameter("gradePrefix", gradePrefix)
                .getResultList();

        List<StudentBehaviorSummaryDTO> list = new ArrayList<>();
        for (Object[] row : results) {
            StudentBehaviorSummaryDTO dto = new StudentBehaviorSummaryDTO();
            dto.setStudentId((String) row[0]);
            dto.setFullName((String) row[1]);
            dto.setClassName((String) row[2]);
            dto.setBehaviorScore(row[3] != null ? ((Number) row[3]).intValue() : null);
            dto.setStatus((String) row[4]);
            list.add(dto);
        }
        return list;
    }
}
