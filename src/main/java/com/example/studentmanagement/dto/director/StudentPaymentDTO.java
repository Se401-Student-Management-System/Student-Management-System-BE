package com.example.studentmanagement.dto.director;

import lombok.Data;

@Data
public class StudentPaymentDTO {
    private String studentId;
    private String fullName;
    private String className;
    private String academicYear;
    private Float totalFee;
    private Float paidAmount;
    private Float outstandingAmount;
    private String status;
}