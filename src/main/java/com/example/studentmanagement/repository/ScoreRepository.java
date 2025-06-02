package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

    // Truy vấn tuỳ chỉnh bằng JPQL - theo studentId
    @Query("SELECT s FROM Score s WHERE s.student.id = ?1 AND s.semester = ?2 AND s.academicYear = ?3")
    List<Score> findScoresByStudentAndSemester(String studentId, int semester, String academicYear);

    // Truy vấn tự động bằng Spring Data JPA - theo Student entity
    List<Score> findByStudentAndSemesterAndAcademicYear(Student student, Integer semester, String academicYear);

    Optional<Score> findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(
        String studentId, Integer subjectId, Integer semester, String academicYear
    );
}
