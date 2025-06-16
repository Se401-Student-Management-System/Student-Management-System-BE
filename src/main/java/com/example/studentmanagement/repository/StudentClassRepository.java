package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.example.studentmanagement.model.Class;

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
    List<StudentClass> findByClassNameAndAcademicYear(@Param("className") String className, @Param("academicYear") String academicYear);

    Optional<StudentClass> findByStudentAndClazzAndAcademicYear(Student student, Class clazz, String academicYear);

    // @Query("SELECT sc FROM StudentClass sc JOIN FETCH sc.clazz WHERE sc.student =
    // :student ORDER BY sc.academicYear DESC")
    List<StudentClass> findByStudentOrderByAcademicYearDesc(Student student);

    // List<StudentClass> findByClazz_Id(String classId);
}
