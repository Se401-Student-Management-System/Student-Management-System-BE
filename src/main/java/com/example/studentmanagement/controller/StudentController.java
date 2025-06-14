package com.example.studentmanagement.controller;

import com.example.studentmanagement.converter.StudentConverter;
import com.example.studentmanagement.designpattern.iterator.IStudentIterator;
import com.example.studentmanagement.dto.student.StudentDTO;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.student.StudentManager;
import com.example.studentmanagement.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PutMapping("/{id}/study")
    public ResponseEntity<?> studyStudent(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performStudy(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error studying student: " + e.getMessage());
        } catch (IllegalStateException e) { // Bắt IllegalStateException nếu hành động không hợp lệ ở trạng thái đó
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot study: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<?> suspendStudent(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performSuspend(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error suspending student: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot suspend: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/warn")
    public ResponseEntity<?> warnStudent(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performWarn(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error warning student: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot warn: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateStudent(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performActivate(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error activating student: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot activate: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/enroll")
    public ResponseEntity<?> enrollStudent(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performEnroll(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error enrolling student: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot enroll: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/leave")
    public ResponseEntity<?> leaveSchool(@PathVariable String id) {
        try {
            StudentDTO updatedStudent = studentService.performLeave(id);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error student leaving school: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot leave school: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

//    ITERATOR
    @Autowired
    private StudentManager studentManager;

    @Autowired
    private StudentConverter studentConverter;

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
}
