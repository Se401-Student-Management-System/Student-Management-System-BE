package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.teacher.ScoreRequest;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.service.teacher.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/enter-scores")
    public ResponseEntity<?> enterScores(@RequestBody ScoreRequest scoreRequest) {
        try {
            Teacher teacher = getCurrentTeacher(); // Lấy Teacher từ database với mã GV001
            List<Score> savedScores = scoreService.enterScores(scoreRequest, teacher);
            return ResponseEntity.ok(savedScores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    private Teacher getCurrentTeacher() {
        // Lấy Teacher từ database với mã GV001
        Optional<Teacher> teacher = teacherRepository.findById("GV001");
        return teacher.orElseThrow(() -> new IllegalArgumentException("Giáo viên GV001 không tồn tại trong database"));
    }
}