package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class WarningState extends AbstractStudentState {

    public WarningState(Student student) {
        super(student);
    }

    @Override
    public void enroll(Student student) { // Ghi danh lại từ Warning (nếu đã giải quyết vấn đề)
        System.out.println("Student " + student.getId()
                + " is enrolling from Warning. Moving to Active state (warning resolved).");
        student.setCurrentState(student.getActiveState());
    }

    @Override
    public void warn(Student student) {
        System.out.println("Student " + student.getId() + " is already in Warning state. Re-warning action.");
        // Có thể tăng mức độ cảnh cáo hoặc không làm gì
    }

    @Override
    public StudyStatus getStatusName() {
        return StudyStatus.WARNING;
    }
}
