package com.example.studentmanagement.dto.cashier;

import lombok.Data;

@Data
public class PaymentStatisticsDTO {
    private int totalStudents;
    private double totalStudentsChangePercent;
    private int paidStudents;
    private double paidStudentsChangePercent;
    private int partiallyPaidStudents;
    private double partiallyPaidStudentsChangePercent;
    private int unpaidStudents;
    private double unpaidStudentsChangePercent;
}