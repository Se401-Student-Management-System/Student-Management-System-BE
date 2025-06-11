package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class WarningState  extends AbstractStudentState {

    public WarningState(Student student) {
        super(student);
    }

    @Override
    public void study(Student student) {
        System.out.println("Student " + student.getId() + " is warned, but still studying.");
        // Vẫn có thể học khi bị cảnh cáo, không thay đổi trạng thái
    }

    @Override
    public void activate(Student student) { // Xóa cảnh cáo và trở lại Active
        System.out.println("Student " + student.getId() + " warning resolved. Moving from Warning to Active state.");
        student.setCurrentState(student.getActiveState());
    }

    @Override
    public void suspend(Student student) {
        System.out.println("Student " + student.getId() + " is suspended due to warning. Moving to Inactive state.");
        student.setCurrentState(student.getInactiveState());
    }

    @Override
    public void leave(Student student) {
        System.out.println("Student " + student.getId() + " leaves school from Warning state. Moving to Inactive state.");
        student.setCurrentState(student.getInactiveState());
    }

    @Override
    public StudyStatus getStatusName() { return StudyStatus.WARNING; }
}
