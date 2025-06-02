package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Integer> {

    // Tìm thông tin StudentClass theo đối tượng Student và năm học
    Optional<StudentClass> findByStudentAndAcademicYear(Student student, String academicYear);

    // Tìm thông tin StudentClass theo studentId (chuỗi) và năm học
    @Query("SELECT sc FROM StudentClass sc JOIN sc.clazz c WHERE sc.student.id = ?1 AND sc.academicYear = ?2")
    Optional<StudentClass> findByStudentIdAndAcademicYear(String studentId, String academicYear);

    // Tìm danh sách StudentClass theo tên lớp và năm học
    @Query("SELECT sc FROM StudentClass sc WHERE sc.clazz.className = :className AND sc.academicYear = :academicYear")
    List<StudentClass> findByClassNameAndAcademicYear(String className, String academicYear);
}
