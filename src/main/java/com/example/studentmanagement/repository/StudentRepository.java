package com.example.studentmanagement.repository;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    // Truy vấn tìm học sinh theo tên lớp và năm học
    @Query("SELECT DISTINCT s FROM Student s " +
            "JOIN s.account a " +
            "JOIN StudentClass sc ON sc.student.id = s.id " +
            "JOIN sc.clazz c " +
            "WHERE c.className LIKE :gradePrefix || '%' AND sc.academicYear = :academicYear")
    List<Student> findStudentsByGradeAndYear(@Param("gradePrefix") String gradePrefix, @Param("academicYear") String academicYear);
    
    // Tìm học sinh theo ID
    Optional<Student> findById(String id);

    // Corrected method to use StudyStatus enum
    List<Student> findByStatus(StudyStatus status);
}