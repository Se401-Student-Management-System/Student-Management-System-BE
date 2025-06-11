package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class StudentStateFactory {
    public static StudentState getState(StudyStatus status, Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student context cannot be null for state creation.");
        }
        switch (status) {
            case ACTIVE:
                return student.getActiveState();
            case PENDING:
                return student.getPendingState();
            case WARNING:
                return student.getWarningState();
            case INACTIVE:
                return student.getInactiveState();
            default:
                throw new IllegalArgumentException("Unknown student status: " + status);
        }
    }
}
