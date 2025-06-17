package com.example.studentmanagement.dto.cashier;

import java.time.LocalDate;

public class InvoiceDetailDTO {
    private Integer invoiceId;
    private String studentId;
    private String studentName;
    private String className;
    private String academicYear;
    private Float totalFee;
    private Float paidAmount;
    private Float outstandingAmount;
    private String status;
    private LocalDate lastPaymentDate;
    private String cashierName;

    // Constructor mặc định
    public InvoiceDetailDTO() {}

    // Constructor đầy đủ
    public InvoiceDetailDTO(
        Integer invoiceId,
        String studentId,
        String studentName,
        String className,
        String academicYear,
        Float totalFee,
        Float paidAmount,
        Float outstandingAmount,
        String status,
        LocalDate lastPaymentDate,
        String cashierName
    ) {
        this.invoiceId = invoiceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.academicYear = academicYear;
        this.totalFee = totalFee;
        this.paidAmount = paidAmount;
        this.outstandingAmount = outstandingAmount;
        this.status = status;
        this.lastPaymentDate = lastPaymentDate;
        this.cashierName = cashierName;
    }

    // Getters và Setters
    public Integer getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Float getTotalFee() { return totalFee; }
    public void setTotalFee(Float totalFee) { this.totalFee = totalFee; }
    public Float getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Float paidAmount) { this.paidAmount = paidAmount; }
    public Float getOutstandingAmount() { return outstandingAmount; }
    public void setOutstandingAmount(Float outstandingAmount) { this.outstandingAmount = outstandingAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
}