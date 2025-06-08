package com.example.studentmanagement.dto.director;

public class CashierRequest extends UserRequest {
    private String status; // Giá trị enum WorkingStatus

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    
}
