package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.PaymentReceipt;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Integer> {
    @Query("SELECT pr FROM PaymentReceipt pr WHERE pr.invoice.id = :invoiceId ORDER BY pr.paymentDate DESC")
    Optional<PaymentReceipt> findTopByInvoiceId(@Param("invoiceId") Integer invoiceId);

    // Phương thức hiện có (nếu có): findTopByInvoiceId
    PaymentReceipt findTopByInvoiceIdOrderByPaymentDateDesc(Integer invoiceId);

    // Thêm phương thức mới để lấy danh sách theo invoiceId và sắp xếp
    @Query("SELECT pr FROM PaymentReceipt pr WHERE pr.invoice.id = ?1 ORDER BY pr.paymentDate DESC")
    List<PaymentReceipt> findByInvoiceIdOrderByPaymentDateDesc(Integer invoiceId);
}
