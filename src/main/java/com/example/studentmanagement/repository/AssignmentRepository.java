package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Assignment;
import com.example.studentmanagement.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    // Truy vấn tự động: tìm tất cả phân công giảng dạy theo lớp, học kỳ, năm học
    List<Assignment> findByClazzAndSemesterAndAcademicYear(Class clazz, Integer semester, String academicYear);

    // Truy vấn tùy chỉnh: lấy danh sách ID môn học được phân công theo tên lớp (LIKE), học kỳ và năm học
    @Query("SELECT DISTINCT a.subject.id FROM Assignment a " +
        "WHERE a.clazz.className LIKE ?1 AND a.semester = ?2 AND a.academicYear = ?3")
    List<Integer> findAssignedSubjectIds(String gradePrefix, int semester, String academicYear);

    @Query("SELECT DISTINCT a.subject.id, a.subject.subjectName FROM Assignment a " +
        "WHERE a.teacher.id = ?1 AND a.academicYear = ?2 AND a.semester = ?3")
    List<Object[]> findSubjectsByTeacher(String teacherId, String academicYear, int semester);
}
