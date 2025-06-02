package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @Column(name = "academic_year", length = 9)
    private String academicYear;

    @Column(name = "total_fee")
    private Float totalFee;

    @Column(name = "paid_amount")
    private Float paidAmount;

    @Column(name = "outstanding_amount")
    private Float outstandingAmount;

    @Column(length = 20)
    private String status; // Đã thanh toán, Chưa thanh toán, Thanh toán 1 phần
}

