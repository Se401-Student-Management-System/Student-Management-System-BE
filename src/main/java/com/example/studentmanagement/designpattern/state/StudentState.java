package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public interface StudentState {
//    void addStudent(Student student);

    void study(Student student);     // Hành động "học tập"
    void suspend(Student student);   // Hành động "bảo lưu" (chuyển sang Inactive)
    void warn(Student student);      // Hành động "cảnh cáo" (chuyển sang Warning)
    void activate(Student student);  // Hành động "kích hoạt" (chuyển sang Active)
    void enroll(Student student);    // Hành động "ghi danh" (từ Pending sang Active)
    void leave(Student student);     // Hành động "nghỉ học" (chuyển sang Inactive)

    StudyStatus getStatusName();
}
