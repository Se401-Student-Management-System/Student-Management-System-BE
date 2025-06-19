package com.example.studentmanagement.service.supervisor;

import com.example.studentmanagement.dto.supervisor.StudentBehaviorSummaryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentBehaviorSummaryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StudentBehaviorSummaryDTO> getStudentBehaviorSummary(int grade, int semester, String academicYear) {
        String sql = "SELECT s.id, a.full_name, c.class_name, "
                + "COUNT(v.id) AS violation_count, "
                + "b.behavior_score, b.status "
                + "FROM student s "
                + "JOIN account a ON s.account_id = a.id "
                + "JOIN student_class sc ON s.id = sc.student_id AND sc.academic_year = :year "
                + "JOIN class c ON sc.class_id = c.id "
                + "JOIN behavior b ON s.id = b.student_id AND b.semester = :semester AND b.academic_year = :year "
                + "LEFT JOIN violation v ON s.id = v.student_id AND v.semester = :semester AND v.academic_year = :year "
                + "WHERE c.class_name LIKE :gradePrefix "
                + "GROUP BY s.id, a.full_name, c.class_name, b.behavior_score, b.status "
                + "ORDER BY b.behavior_score DESC, violation_count DESC";
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
            dto.setViolationCount(row[3] != null ? ((Number) row[3]).longValue() : 0L);
            dto.setBehaviorScore(row[4] != null ? ((Number) row[4]).intValue() : null);
            dto.setStatus((String) row[5]);
            list.add(dto);

        }

        return list;
    }

}
