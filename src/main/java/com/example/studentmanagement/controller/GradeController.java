package com.example.studentmanagement.controller;

import com.example.studentmanagement.designpattern.templatemethod.TeacherGradeReport;
import com.example.studentmanagement.dto.GradeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private TeacherGradeReport teacherGradeReport;

    @GetMapping("/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGrades(
            @PathVariable String studentId,
            @RequestParam String userId,
            @RequestParam String role,
            @RequestParam int semester,
            @RequestParam String academicYear
    ) {
        Map<String, Object> params = Map.of(
                "userId", userId,
                "role", role,
                "studentId", studentId,
                "semester", semester,
                "academicYear", academicYear
        );
        @SuppressWarnings("unchecked")
        List<GradeResponse> grades = (List<GradeResponse>) teacherGradeReport.generateReport(params);
        return ResponseEntity.ok(grades);
    }
}
