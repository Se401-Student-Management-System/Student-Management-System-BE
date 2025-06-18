package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class ActiveState extends AbstractStudentState {
    public ActiveState(Student student) {
        super(student);
    }

    @Override
    public void enroll(Student student) {
        System.out.println("Student " + student.getId() + " is already Active. No re-enrollment needed.");
    }

    @Override
    public void warn(Student student) {
        System.out.println("Student " + student.getId() + " is warned. Moving to Warning state.");
        student.setCurrentState(student.getWarningState()); // Chuyển sang Warning
    }

    @Override
    public StudyStatus getStatusName() {
        return StudyStatus.ACTIVE;
    }
}
