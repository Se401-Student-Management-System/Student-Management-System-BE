package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment_receipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cashier_id", referencedColumnName = "id")
    private Cashier cashier;

    @Column(name = "paid_amount")
    private Float paidAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;
}
