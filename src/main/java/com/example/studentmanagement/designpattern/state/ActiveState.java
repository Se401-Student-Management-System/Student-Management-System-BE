package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class ActiveState extends AbstractStudentState {
    public ActiveState(Student student) {
        super(student);
    }


    @Override
    public void study(Student student) {
        System.out.println("Student " + student.getId() + " is actively studying.");
        // Không thay đổi trạng thái, chỉ thực hiện hành vi "học"
    }

    @Override
    public void suspend(Student student) {
        System.out.println("Student " + student.getId() + " is suspended. Moving to Inactive state (Bảo lưu).");
        student.setCurrentState(student.getInactiveState()); // Chuyển sang Inactive
    }

    @Override
    public void warn(Student student) {
        System.out.println("Student " + student.getId() + " is warned. Moving to Warning state.");
        student.setCurrentState(student.getWarningState()); // Chuyển sang Warning
    }

    @Override
    public void activate(Student student) {
        System.out.println("Student " + student.getId() + " is already active.");
    }

    @Override
    public void leave(Student student) {
        System.out.println("Student " + student.getId() + " leaves school. Moving to Inactive state (Nghỉ học).");
        student.setCurrentState(student.getInactiveState()); // Chuyển sang Inactive
    }


    @Override
    public StudyStatus getStatusName() {
        return StudyStatus.ACTIVE;
    }
}
