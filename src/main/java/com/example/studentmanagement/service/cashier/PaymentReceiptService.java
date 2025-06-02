package com.example.studentmanagement.service.cashier;

import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import com.example.studentmanagement.dto.cashier.PaymentStatisticsDTO;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.model.Invoice;
import com.example.studentmanagement.model.PaymentReceipt;
import com.example.studentmanagement.repository.CashierRepository;
import com.example.studentmanagement.repository.InvoiceRepository;
import com.example.studentmanagement.repository.PaymentReceiptRepository;
import com.example.studentmanagement.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentReceiptService {

    @Autowired
    private PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CashierRepository cashierRepository;

    @Transactional
    public PaymentReceipt addPaymentRecord(String invoiceId, String cashierId, float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền đóng không hợp lệ");
        }

        Invoice invoice = invoiceRepository.findById(Integer.parseInt(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));

        if ("Đã thanh toán".equals(invoice.getStatus())) {
            throw new IllegalStateException("Invoice is already fully paid: " + invoiceId);
        }

        Cashier cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found: " + cashierId));

        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setInvoice(invoice);
        receipt.setCashier(cashier);
        receipt.setPaidAmount(amount);
        receipt.setPaymentDate(LocalDate.now());
        PaymentReceipt savedReceipt = paymentReceiptRepository.save(receipt);

        updateInvoice(invoice, amount);

        return savedReceipt;
    }

    private void updateInvoice(Invoice invoice, float paymentAmount) {
        float newPaidAmount = invoice.getPaidAmount() != null ? invoice.getPaidAmount() + paymentAmount : paymentAmount;
        float debt = invoice.getTotalFee() - newPaidAmount;
        String status;
        if (newPaidAmount >= invoice.getTotalFee()) {
            status = "Đã thanh toán";
        } else if (newPaidAmount > 0) {
            status = "Thanh toán 1 phần";
        } else {
            status = "Chưa thanh toán";
        }
        invoice.setPaidAmount(newPaidAmount);
        invoice.setOutstandingAmount(debt);
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }

    @Transactional(readOnly = true)
    public List<PaymentRecordDTO> exportPaymentRecords(String academicYear, String status) {
        List<Invoice> invoices;
        if (academicYear != null && status != null) {
            invoices = invoiceRepository.findByAcademicYearAndStatus(academicYear, status);
        } else if (academicYear != null) {
            invoices = invoiceRepository.findByAcademicYear(academicYear);
        } else if (status != null) {
            invoices = invoiceRepository.findByStatus(status);
        } else {
            invoices = invoiceRepository.findAll();
        }

        List<PaymentRecordDTO> records = new ArrayList<>();
        for (Invoice invoice : invoices) {
            PaymentRecordDTO dto = new PaymentRecordDTO();
            dto.setInvoiceId(invoice.getId());
            dto.setStudentId(invoice.getStudent().getId());
            Student student = invoice.getStudent();
            if (student != null && student.getAccount() != null) {
                dto.setStudentName(student.getAccount().getFullName());
            } else {
                dto.setStudentName("Unknown");
            }
            dto.setAcademicYear(invoice.getAcademicYear());
            dto.setTotalFee(invoice.getTotalFee());
            dto.setPaidAmount(invoice.getPaidAmount());
            dto.setOutstandingAmount(invoice.getOutstandingAmount());
            dto.setStatus(invoice.getStatus()); 
            records.add(dto);
        }
        return records;
    }

    @Transactional(readOnly = true)
    public PaymentStatisticsDTO getPaymentStatistics(String academicYear) {
        List<Invoice> currentYearInvoices = invoiceRepository.findByAcademicYear(academicYear);
        String previousYear = getPreviousAcademicYear(academicYear);
        List<Invoice> previousYearInvoices = previousYear != null ? invoiceRepository.findByAcademicYear(previousYear) : new ArrayList<>();

        Map<String, Invoice> currentStudentInvoices = currentYearInvoices.stream()
                .collect(Collectors.toMap(
                        invoice -> invoice.getStudent().getId(),
                        invoice -> invoice,
                        (existing, replacement) -> existing.getId() > replacement.getId() ? existing : replacement
                ));

        int totalStudentsCurrent = currentStudentInvoices.size();
        int paidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Đã thanh toán".equals(i.getStatus())).count();
        int partiallyPaidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Thanh toán 1 phần".equals(i.getStatus())).count();
        int unpaidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Chưa thanh toán".equals(i.getStatus())).count();

        Map<String, Invoice> previousStudentInvoices = previousYearInvoices.stream()
                .collect(Collectors.toMap(
                        invoice -> invoice.getStudent().getId(),
                        invoice -> invoice,
                        (existing, replacement) -> existing.getId() > replacement.getId() ? existing : replacement
                ));

        int totalStudentsPrevious = previousStudentInvoices.size();
        int paidStudentsPrevious = (int) previousStudentInvoices.values().stream().filter(i -> "Đã thanh toán".equals(i.getStatus())).count();
        int partiallyPaidStudentsPrevious = (int) previousStudentInvoices.values().stream().filter(i -> "Thanh toán 1 phần".equals(i.getStatus())).count();
        int unpaidStudentsPrevious = (int) previousStudentInvoices.values().stream().filter(i -> "Chưa thanh toán".equals(i.getStatus())).count();

        double totalStudentsChangePercent = totalStudentsPrevious > 0 ? ((double) (totalStudentsCurrent - totalStudentsPrevious) / totalStudentsPrevious) * 100 : 0.0;
        double paidStudentsChangePercent = paidStudentsPrevious > 0 ? ((double) (paidStudentsCurrent - paidStudentsPrevious) / paidStudentsPrevious) * 100 : 0.0;
        double partiallyPaidStudentsChangePercent = partiallyPaidStudentsPrevious > 0 ? ((double) (partiallyPaidStudentsCurrent - partiallyPaidStudentsPrevious) / partiallyPaidStudentsPrevious) * 100 : 0.0;
        double unpaidStudentsChangePercent = unpaidStudentsPrevious > 0 ? ((double) (unpaidStudentsCurrent - unpaidStudentsPrevious) / unpaidStudentsPrevious) * 100 : 0.0;

        PaymentStatisticsDTO stats = new PaymentStatisticsDTO();
        stats.setTotalStudents(totalStudentsCurrent);
        stats.setTotalStudentsChangePercent(totalStudentsChangePercent);
        stats.setPaidStudents(paidStudentsCurrent);
        stats.setPaidStudentsChangePercent(paidStudentsChangePercent);
        stats.setPartiallyPaidStudents(partiallyPaidStudentsCurrent);
        stats.setPartiallyPaidStudentsChangePercent(partiallyPaidStudentsChangePercent);
        stats.setUnpaidStudents(unpaidStudentsCurrent);
        stats.setUnpaidStudentsChangePercent(unpaidStudentsChangePercent);

        return stats;
    }

    private String getPreviousAcademicYear(String currentYear) {
        try {
            String[] years = currentYear.split("-");
            int currentStart = Integer.parseInt(years[0]);
            int currentEnd = Integer.parseInt(years[1]);
            int prevStart = currentStart - 1;
            int prevEnd = currentEnd - 1;
            return prevStart + "-" + prevEnd;
        } catch (Exception e) {
            return null;
        }
    }
}