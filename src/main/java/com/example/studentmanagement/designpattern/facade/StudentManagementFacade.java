package com.example.studentmanagement.designpattern.facade;

import com.example.studentmanagement.service.director.StudentConductStatics;
import com.example.studentmanagement.service.director.StudentNumberStatics;
import com.example.studentmanagement.service.director.StudentScoreStatics;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentManagementFacade {

    private final StudentScoreStatics studentScoreStatics;
    private final StudentNumberStatics studentNumberStatics;
    private final StudentConductStatics studentConductStatics;

    public StudentManagementFacade(StudentScoreStatics studentScoreStatics,
                                   StudentNumberStatics studentNumberStatics,
                                   StudentConductStatics studentConductStatics) {
        this.studentScoreStatics = studentScoreStatics;
        this.studentNumberStatics = studentNumberStatics;
        this.studentConductStatics = studentConductStatics;
    }

    public Map<String, Object> getStudentOverview(String academicYear) {
        Map<String, Object> overview = new HashMap<>();
        overview.put("academicYear", academicYear);
        overview.put("enrolledStats", studentNumberStatics.getEnrolledStatistics(academicYear));
        overview.put("totalStats", studentNumberStatics.getActiveStatistics(0, academicYear));
        overview.put("warnedStats", studentNumberStatics.getWarnedStatistics(0, academicYear));
        overview.put("enrolledHistory", studentNumberStatics.getEnrolledStudentHistory());
        overview.put("warnedHistory", studentNumberStatics.getWarnedStudentHistory());
        return overview;
    }

    public Map<String, Object> getGradeOverview(int grade, int semester, String academicYear) {
        Map<String, Object> overview = new HashMap<>();
        overview.put("grade", "Khối " + grade);
        overview.put("semester", semester == 1 ? "Học kỳ 1" : "Học kỳ 2");
        overview.put("academicYear", academicYear);
        overview.put("goodGrade", studentScoreStatics.getGoodGradeStatistics(grade, semester, academicYear));
        overview.put("normalGrade", studentScoreStatics.getNormalGradeStatistics(grade, semester, academicYear));
        overview.put("mediumGrade", studentScoreStatics.getMediumGradeStatistics(grade, semester, academicYear));
        overview.put("weakGrade", studentScoreStatics.getWeakGradeStatistics(grade, semester, academicYear));
        overview.put("topTenStudents", studentScoreStatics.getTopTenStudents(grade, semester, academicYear));
        return overview;
    }

    public Map<String, Object> getConductOverview(int semester, String academicYear) {
        Map<String, Object> overview = new HashMap<>();
        overview.put("semester", semester == 1 ? "Học kỳ 1" : "Học kỳ 2");
        overview.put("academicYear", academicYear);
        overview.put("grade10Conduct", studentConductStatics.countConductRank(10, semester, academicYear));
        overview.put("grade11Conduct", studentConductStatics.countConductRank(11, semester, academicYear));
        overview.put("grade12Conduct", studentConductStatics.countConductRank(12, semester, academicYear));
        overview.put("lowestConductStudents", studentConductStatics.getLowestConductStudents(semester, academicYear));
        return overview;
    }
}