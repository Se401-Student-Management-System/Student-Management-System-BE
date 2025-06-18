package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public interface StudentState {
    void warn(Student student); // Hành động "cảnh cáo" (chuyển sang Warning)

    void enroll(Student student); // Hành động "ghi danh" (từ Pending sang Active

    StudyStatus getStatusName();
}
