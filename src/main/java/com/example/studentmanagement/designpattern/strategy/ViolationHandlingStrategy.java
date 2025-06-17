package com.example.studentmanagement.designpattern.strategy;

import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Violation;

public interface ViolationHandlingStrategy {
    ViolationResponse handleViolation(Student student, Violation violation);
}
