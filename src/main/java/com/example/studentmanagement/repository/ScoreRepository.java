package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.dto.student.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT DISTINCT s.student FROM Score s WHERE s.teacher.id = :teacherId")
    List<Student> findStudentsByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT DISTINCT new com.example.studentmanagement.dto.student.StudentDTO(" +
           "s.student.id, " +
           "s.student.account.username, " +
           "s.student.account.fullName, " +
           "s.student.account.email, " +
           "s.student.account.phoneNumber, " +
           "s.student.account.address, " +
           "CAST(s.student.account.gender AS string), " + // enum -> String
           "s.student.account.birthDate, " +
           "s.student.account.role.roleName, " +
           "s.student.ethnicity, " +
           "s.student.birthPlace, " +
           "CAST(s.student.status AS string), " + // enum -> String
           "sc.clazz.className, " +
           "s.academicYear" +
           ") " +
           "FROM Score s " +
           "JOIN StudentClass sc ON sc.student.id = s.student.id AND sc.academicYear = s.academicYear " +
           "WHERE s.teacher.id = :teacherId")
    List<StudentDTO> findStudentDTOsByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT s FROM Score s " +
           "JOIN s.student st " +
           "JOIN st.account a " +
           "JOIN s.teacher t " +
           "JOIN s.subject sub " +
           "JOIN st.studentClasses sc " +
           "JOIN sc.clazz c " +
           "JOIN t.account ta " +
           "WHERE t.id = :teacherId " +
           "AND c.className = :className " +
           "AND sub.subjectName = :subjectName " +
           "AND s.semester = :semester " +
           "AND s.academicYear = :academicYear " +
           "AND sc.academicYear = :academicYear") // Đảm bảo khớp năm học
    List<Score> findScoreInputDetail(
        @Param("teacherId") String teacherId,
        @Param("className") String className,
        @Param("subjectName") String subjectName,
        @Param("semester") Integer semester,
        @Param("academicYear") String academicYear
    );
}