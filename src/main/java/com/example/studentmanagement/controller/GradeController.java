package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.GradeResponse;
import com.example.studentmanagement.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGrades(
            @PathVariable String studentId,
            @RequestParam String userId,              // user đang đăng nhập (teacher hoặc student)
            @RequestParam String role,        
            @RequestParam int semester,
            @RequestParam String academicYear 
    ) {
        List<GradeResponse> grades = gradeService.getGrades(userId, role, studentId, semester, academicYear);
        return ResponseEntity.ok(grades);
    }
}