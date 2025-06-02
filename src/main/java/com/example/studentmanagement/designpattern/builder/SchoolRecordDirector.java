package com.example.studentmanagement.designpattern.builder;

import com.example.studentmanagement.dto.director.SchoolRecord;
import com.example.studentmanagement.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SchoolRecordDirector {
    private final SchoolRecordBuilder builder;

    public SchoolRecordDirector(SchoolRecordBuilder builder) {
        this.builder = builder;
    }

    public SchoolRecord constructSchoolRecord(String studentId, int semester, String academicYear, String comment) {
        Student student = builder.getStudentRepository().findById(studentId)
                .orElseThrow(() -> new RuntimeException("Học sinh không tồn tại"));
        StudentClass studentClass = builder.getStudentClassRepository()
                .findByStudentAndAcademicYear(student, academicYear)
                .orElseThrow(() -> new RuntimeException("Lớp học của học sinh không tồn tại"));

        SchoolRecordBuilder recordBuilder = builder
                .setStudentInfo(student, student.getAccount().getFullName(), studentClass.getClazz().getClassName());

        if (semester == 0) {
            recordBuilder
                    .setScores(student, 1, academicYear)
                    .calculateAverageScore(1, academicYear)
                    .setScores(student, 2, academicYear)
                    .calculateAverageScore(2, academicYear)
                    .calculateYearlyAveragePerSubject(academicYear)
                    .setBehaviorScore(student, 2, academicYear)
                    .setAcademicPerformance()
                    .setTitle()
                    .comment(comment);
        } else {
            recordBuilder
                    .setScores(student, semester, academicYear)
                    .calculateAverageScore(semester, academicYear)
                    .calculateYearlyAveragePerSubject(academicYear)
                    .setBehaviorScore(student, semester, academicYear)
                    .setAcademicPerformance()
                    .setTitle()
                    .comment(comment);
        }

        return recordBuilder.build();
    }

    public List<SchoolRecord> constructSchoolRecordsForClass(String className, int semester, String academicYear) {
        List<StudentClass> studentClasses = builder.getStudentClassRepository()
                .findByClassNameAndAcademicYear(className, academicYear);
        List<SchoolRecord> records = new ArrayList<>();

        for (StudentClass sc : studentClasses) {
            SchoolRecord record = constructSchoolRecord(sc.getStudent().getId(), semester, academicYear, null);
            records.add(record);
        }
        return records;
    }
}