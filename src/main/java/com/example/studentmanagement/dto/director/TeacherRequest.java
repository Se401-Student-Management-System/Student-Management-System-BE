package com.example.studentmanagement.dto.director;

public class TeacherRequest extends UserRequest {
    private String position; // Giá trị enum TeacherPosition
    private String status;   // Giá trị enum TeachingStatus

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
