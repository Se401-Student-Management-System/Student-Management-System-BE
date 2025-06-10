package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public class InactiveState extends  AbstractStudentState{
    public InactiveState(Student student) {
        super(student);
    }

    @Override
    public void study(Student student) {
        System.out.println("Student " + student.getId() + " is inactive. Cannot study.");
        throw new IllegalStateException("Học sinh nghỉ học không thể học.");
    }

    @Override
    public void activate(Student student) { // Kích hoạt lại từ bảo lưu/nghỉ học
        System.out.println("Student " + student.getId() + " activated. Moving from Inactive to Active state.");
        student.setCurrentState(student.getActiveState());
    }


    @Override
    public StudyStatus getStatusName() { return StudyStatus.INACTIVE; }

}
