package com.example.studentmanagement.designpattern.builder;

import com.example.studentmanagement.dto.director.SchoolRecord;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.AssignmentRepository;
import com.example.studentmanagement.repository.BehaviorRepository;
import com.example.studentmanagement.repository.ScoreRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.TitleRepository;

public interface SchoolRecordBuilder {
    SchoolRecordBuilder setStudentInfo(Student student, String studentName, String className);
    SchoolRecordBuilder setScores(Student student, int semester, String academicYear);
    SchoolRecordBuilder calculateAverageScore(int semester, String academicYear);
    SchoolRecordBuilder calculateYearlyAverageScore(String academicYear);
    SchoolRecordBuilder setBehaviorScore(Student student, int semester, String academicYear);
    SchoolRecordBuilder setAcademicPerformance();
    SchoolRecordBuilder setTitle();
    SchoolRecordBuilder comment(String comment);
    SchoolRecord build();

    // Phương thức đã có
    SchoolRecordBuilder calculateYearlyAveragePerSubject(String academicYear);

    // Thêm phương thức mới
    SchoolRecordBuilder setAverageScore(int semester, Double averageScore);

    // Getters cho repository
    StudentRepository getStudentRepository();
    StudentClassRepository getStudentClassRepository();
    BehaviorRepository getBehaviorRepository();
    ScoreRepository getScoreRepository();
    TitleRepository getTitleRepository();
    AssignmentRepository getAssignmentRepository();
}