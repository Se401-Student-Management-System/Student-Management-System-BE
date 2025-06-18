package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.behavior.BehaviorEvaluationResponse;
import com.example.studentmanagement.dto.behavior.BehaviorEvalutionRequest;
import com.example.studentmanagement.dto.student.AssignClassRequest;
import com.example.studentmanagement.dto.student.StudentDTO;
import com.example.studentmanagement.dto.student.UpdateStudentRequest;
import com.example.studentmanagement.service.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // STATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody @Valid UpdateStudentRequest request) {
        try {
            StudentDTO updatedStudent = studentService.updateStudent(id, request);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật sinh viên: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi cập nhật sinh viên: " + e.getMessage());
        }
    }

    @PutMapping("/assign-class")
    public ResponseEntity<?> assignStudentToClass(@RequestBody @Valid AssignClassRequest request) {
        try {
            StudentDTO updatedStudent = studentService.assignStudentToClass(request);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi dữ liệu xếp lớp: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lỗi nghiệp vụ xếp lớp: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi xếp lớp: " + e.getMessage());
        }
    }

    @PutMapping("/evaluate-behavior")
    public ResponseEntity<?> evaluateStudentBehavior(@RequestBody @Valid BehaviorEvalutionRequest request) {
        try {
            BehaviorEvaluationResponse response = studentService.evaluateStudentBehavior(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi dữ liệu đánh giá hạnh kiểm: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Lỗi nghiệp vụ đánh giá hạnh kiểm: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi đánh giá hạnh kiểm: " + e.getMessage());
        }
    }

    // ITERATOR
    @GetMapping("/list-all")
    public ResponseEntity<List<StudentDTO>> getAllStudentsUsingIterator() {
        try {
            List<StudentDTO> students = studentService.getAllStudentsUsingIterator();
            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list-status/{statusName}")
    public ResponseEntity<List<StudentDTO>> getAllStudentsUsingIterator(@PathVariable String statusName) {
        List<StudentDTO> studentDTOList = studentService.getStudentsByStatusUsingIterator(statusName);

        if (studentDTOList.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content nếu không có sinh viên
        }
        return ResponseEntity.ok(studentDTOList); // Trả về danh sách StudentDTO
    }

    // @Autowired
    // private com.example.studentmanagement.repository.ScoreRepository
    // scoreRepository;

    // @GetMapping("/by-teacher/{teacherId}")
    // public ResponseEntity<List<StudentDTO>> getStudentsByTeacher(@PathVariable
    // String teacherId) {
    // List<StudentDTO> students =
    // scoreRepository.findStudentDTOsByTeacherId(teacherId);
    // return ResponseEntity.ok(students);
    // }

    // @GetMapping("/by-subject")
    // public ResponseEntity<List<StudentDTO>> getStudentsBySubject(
    // @RequestParam String subjectId,
    // @RequestParam String academicYear,
    // @RequestParam int semester
    // ) {
    // List<StudentDTO> students = studentService.findStudentsBySubject(subjectId,
    // academicYear, semester);
    // return ResponseEntity.ok(students);
    // }

    @GetMapping("/by-class-and-subject")
    public ResponseEntity<List<StudentDTO>> getStudentsByClassAndSubject(
            @RequestParam String className,
            @RequestParam String subjectId,
            @RequestParam String academicYear,
            @RequestParam int semester) {
        List<StudentDTO> students = studentService.findStudentsByClassAndSubject(className, subjectId, academicYear,
                semester);
        return ResponseEntity.ok(students);
    }
}
