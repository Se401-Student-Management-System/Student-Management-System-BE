package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.teacher.ScoreInputDetailDTO;
import com.example.studentmanagement.dto.teacher.ScoreRequest;
import com.example.studentmanagement.dto.teacher.SubjectDTO;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.service.teacher.ScoreService;
import com.example.studentmanagement.service.teacher.TeacherEvaluationService;
import com.example.studentmanagement.service.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private TeacherEvaluationService evaluationService;

    @PostMapping("/enter-scores")
    public ResponseEntity<?> enterScores(@RequestBody ScoreRequest scoreRequest) {
        try {
            Teacher teacher = getCurrentTeacher();
            List<Score> savedScores = scoreService.enterScores(scoreRequest, teacher);
            return ResponseEntity.ok(savedScores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjectsByTeacher(
            @RequestParam String teacherId,
            @RequestParam String year,
            @RequestParam int semester) {
        List<Object[]> subjects = scoreService.getSubjectsByTeacher(teacherId, year, semester);
        List<SubjectDTO> result = subjects.stream()
                .map(obj -> new SubjectDTO(String.valueOf(obj[0]), (String) obj[1], (String) obj[2]))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/enter-list")
    public ResponseEntity<Map<String, Object>> enterList(
            @RequestParam(value = "semester", required = false, defaultValue = "1") Integer semester,
            @RequestParam(value = "year", required = false) String year) {
        try {
            String teacherId = "GV001";
            Map<String, Object> response = scoreService.getScoreInputInfo(teacherId, semester, year);
            return ResponseEntity.ok(Map.of(
                    "selectedYear", response.get("selectedYear"),
                    "selectedSemester", response.get("selectedSemester"),
                    "enterList", response.get("enterList")
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "selectedYear", "",
                    "selectedSemester", semester != null ? "Học kỳ " + semester : "",
                    "enterList", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "selectedYear", "",
                    "selectedSemester", semester != null ? "Học kỳ " + semester : "",
                    "enterList", new ArrayList<>()
            ));
        }
    }

    @GetMapping("/enter-next")
    public List<ScoreInputDetailDTO> enterNextApi(
            @RequestParam(value = "teacherId", required = false) String teacherId,
            @RequestParam(value = "className", required = false) String className,
            @RequestParam(value = "subjectName", required = false) String subjectName,
            @RequestParam(value = "semester", required = false, defaultValue = "1") Integer semester,
            @RequestParam(value = "academicYear", required = false) String academicYear) {
        if (teacherId == null) {
            teacherId = "GV001";
        }
        return scoreService.getScoreInputDetail(teacherId, className, subjectName, semester, academicYear);
    }

    @PostMapping("/add-comments")
    public ResponseEntity<?> addComments(@RequestBody List<TeacherService.CommentRequest> commentRequests) {
        try {
            Teacher teacher = getCurrentTeacher();
            teacherService.addComments(commentRequests, teacher);
            return ResponseEntity.ok(Map.of("message", "Cập nhật nhận xét thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

    @GetMapping("/class-rate")
    public ResponseEntity<?> getClassRate(
            @RequestParam(value = "year", required = false) String selectedYear,
            @RequestParam(value = "semester", required = false, defaultValue = "Học kỳ 1") String selectedSemester,
            @RequestParam(value = "class", required = false) String selectedClass) {

        try {
            Map<String, Object> response = evaluationService.getStudentListForEvaluation(selectedYear, selectedSemester, selectedClass);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    private Teacher getCurrentTeacher() {
        Optional<Teacher> teacher = teacherRepository.findById("GV001");
        return teacher.orElseThrow(() -> new IllegalArgumentException("Giáo viên GV001 không tồn tại trong database"));
    }
}