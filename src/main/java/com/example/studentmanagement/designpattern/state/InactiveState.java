package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class InactiveState extends AbstractStudentState {
    public InactiveState(Student student) {
        super(student);
    }

    @Override
    public void enroll(Student student) { // Có thể cho phép ghi danh lại từ Inactive
        System.out.println("Student " + student.getId() + " is enrolling from Inactive. Moving to Active state.");
        student.setCurrentState(student.getActiveState());
    }

    @Override
    public void warn(Student student) {
        System.out.println("Student " + student.getId() + " is warned. Moving to Warning state.");
        student.setCurrentState(student.getWarningState()); // Chuyển sang Warning
    }

    @Override
    public StudyStatus getStatusName() {
        return StudyStatus.INACTIVE;
    }

}
