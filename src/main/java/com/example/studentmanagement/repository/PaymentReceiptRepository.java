package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Integer> {
}
