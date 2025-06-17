package com.example.studentmanagement.service.cashier;

import com.example.studentmanagement.dto.cashier.InvoiceDetailDTO;
import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import com.example.studentmanagement.dto.cashier.PaymentStatisticsDTO;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.model.Invoice;
import com.example.studentmanagement.model.PaymentReceipt;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.CashierRepository;
import com.example.studentmanagement.repository.InvoiceRepository;
import com.example.studentmanagement.repository.PaymentReceiptRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PaymentReceiptService {

    @Autowired
    private PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CashierRepository cashierRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Transactional
    public void addPaymentRecord(String cashierId, String invoiceId, float amount) {
        Cashier cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân: " + cashierId));
        Invoice invoice = invoiceRepository.findById(Integer.valueOf(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn: " + invoiceId));

        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền đóng không hợp lệ");
        }
        if (invoice.getOutstandingAmount() <= 0) {
            throw new IllegalStateException("Hóa đơn đã được thanh toán đầy đủ: " + invoiceId);
        }
        if (amount > invoice.getOutstandingAmount()) {
            throw new IllegalArgumentException("Số tiền vượt quá số tiền còn nợ");
        }

        PaymentReceipt paymentReceipt = new PaymentReceipt();
        paymentReceipt.setCashier(cashier);
        paymentReceipt.setInvoice(invoice);
        paymentReceipt.setPaidAmount(amount);
        paymentReceipt.setPaymentDate(LocalDate.now());

        paymentReceiptRepository.save(paymentReceipt);

        invoice.setPaidAmount(invoice.getPaidAmount() + amount);
        invoice.setOutstandingAmount(invoice.getOutstandingAmount() - amount);
        invoice.setStatus(invoice.getOutstandingAmount() == 0 ? "Đã thanh toán" : "Thanh toán 1 phần");
        invoiceRepository.save(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceDetailDTO> getAllInvoices(String status, String academicYear) {
        try {
            List<Invoice> invoices;
            if (status != null && academicYear != null) {
                invoices = invoiceRepository.findByAcademicYearAndStatus(academicYear, status);
            } else if (status != null) {
                invoices = invoiceRepository.findByStatus(status);
            } else if (academicYear != null) {
                invoices = invoiceRepository.findByAcademicYear(academicYear);
            } else {
                invoices = invoiceRepository.findAll();
            }

            List<InvoiceDetailDTO> result = new ArrayList<>();
            for (Invoice invoice : invoices) {
                try {
                    Student student = invoice.getStudent();
                    String studentName = (student != null && student.getAccount() != null) ? student.getAccount().getFullName() : "Unknown";
                    String className = "Unknown";

                    if (student != null) {
                        AtomicReference<String> tempClassName = new AtomicReference<>("Unknown");
                        studentClassRepository.findByStudentAndAcademicYear(student, invoice.getAcademicYear())
                            .ifPresent(sc -> {
                                if (sc.getClazz() != null) {
                                    tempClassName.set(sc.getClazz().getClassName());
                                }
                            });
                        className = tempClassName.get();
                    }

                    LocalDate lastPaymentDate = null;
                    try {
                        List<PaymentReceipt> paymentReceipts = paymentReceiptRepository.findByInvoiceIdOrderByPaymentDateDesc(invoice.getId());
                        if (!paymentReceipts.isEmpty()) {
                            lastPaymentDate = paymentReceipts.get(0).getPaymentDate();
                        }
                    } catch (Exception ignored) {}

                    InvoiceDetailDTO dto = new InvoiceDetailDTO(
                        invoice.getId(),
                        student != null ? student.getId() : null,
                        studentName,
                        className,
                        invoice.getAcademicYear(),
                        invoice.getTotalFee(),
                        invoice.getPaidAmount(),
                        invoice.getOutstandingAmount(),
                        invoice.getStatus(),
                        lastPaymentDate,
                        null
                    );
                    result.add(dto);
                } catch (Exception e) {
                    InvoiceDetailDTO dto = new InvoiceDetailDTO(
                        invoice.getId(),
                        null,
                        "Unknown",
                        "Unknown",
                        invoice.getAcademicYear(),
                        invoice.getTotalFee(),
                        invoice.getPaidAmount(),
                        invoice.getOutstandingAmount(),
                        invoice.getStatus(),
                        null,
                        null
                    );
                    result.add(dto);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public InvoiceDetailDTO getInvoiceById(Integer invoiceId) {
        try {
            Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
            if (invoice == null) return null;

            Student student = invoice.getStudent();
            String studentName = (student != null && student.getAccount() != null) ? student.getAccount().getFullName() : "Unknown";
            String className = "Unknown";

            if (student != null) {
                AtomicReference<String> tempClassName = new AtomicReference<>("Unknown");
                studentClassRepository.findByStudentAndAcademicYear(student, invoice.getAcademicYear())
                    .ifPresent(sc -> {
                        if (sc.getClazz() != null) {
                            tempClassName.set(sc.getClazz().getClassName());
                        }
                    });
                className = tempClassName.get();
            }

            LocalDate lastPaymentDate = paymentReceiptRepository
                .findTopByInvoiceId(invoiceId)
                .map(PaymentReceipt::getPaymentDate)
                .orElse(null);

            return new InvoiceDetailDTO(
                invoice.getId(),
                student != null ? student.getId() : null,
                studentName,
                className,
                invoice.getAcademicYear(),
                invoice.getTotalFee(),
                invoice.getPaidAmount(),
                invoice.getOutstandingAmount(),
                invoice.getStatus(),
                lastPaymentDate,
                null
            );
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage(), e);
        }
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
            dto.setStudentId(invoice.getStudent() != null ? invoice.getStudent().getId() : null);
            Student student = invoice.getStudent();
            dto.setStudentName((student != null && student.getAccount() != null) ? student.getAccount().getFullName() : "Unknown");
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
                        invoice -> invoice.getStudent() != null ? invoice.getStudent().getId() : "",
                        invoice -> invoice,
                        (existing, replacement) -> existing.getId() > replacement.getId() ? existing : replacement
                ));

        int totalStudentsCurrent = currentStudentInvoices.size();
        int paidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Đã thanh toán".equals(i.getStatus())).count();
        int partiallyPaidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Thanh toán 1 phần".equals(i.getStatus())).count();
        int unpaidStudentsCurrent = (int) currentStudentInvoices.values().stream().filter(i -> "Chưa thanh toán".equals(i.getStatus())).count();

        Map<String, Invoice> previousStudentInvoices = previousYearInvoices.stream()
                .collect(Collectors.toMap(
                        invoice -> invoice.getStudent() != null ? invoice.getStudent().getId() : "",
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
