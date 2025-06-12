package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.designpattern.singleton.StudentRecordManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentInfoController {

    @GetMapping("/profile/{id}")
    public ResponseEntity<Student> getStudentProfile(@PathVariable String id) {
        Student student = StudentRecordManager.getInstance().getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
}
