package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Behavior;
import com.example.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {
    Optional<Behavior> findByStudentAndSemesterAndAcademicYear(Student student, Integer semester, String academicYear);

    Optional<Behavior> findByStudentAndAcademicYearAndSemester(Student student, String academicYear, Integer semester);
}
