package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudentStatus;
import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class PendingState extends AbstractStudentState{
    public PendingState(Student student) {
        super(student);
    }

    @Override
    public void study(Student student) {
        System.out.println("Student " + student.getId() + " is pending. Cannot study yet.");
        throw new IllegalStateException("Học sinh mới tiếp nhận không thể học.");
    }

    @Override
    public void enroll(Student student) { // Hành động "ghi danh"
        System.out.println("Student " + student.getId() + " enrolled. Moving from Pending to Active state.");
        student.setCurrentState(student.getActiveState()); // Chuyển sang Active
    }

    @Override
    public void warn(Student student) { // Có thể cảnh cáo cả học sinh đang Pending (ví dụ: thiếu giấy tờ)
        System.out.println("Student " + student.getId() + " is warned. Moving from Pending to Warning state.");
        student.setCurrentState(student.getWarningState());
    }


    @Override
    public StudyStatus getStatusName() { return StudyStatus.PENDING; }
}
