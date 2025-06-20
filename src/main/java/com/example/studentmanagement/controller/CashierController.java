package com.example.studentmanagement.controller;

import com.example.studentmanagement.designpattern.command.AddPaymentRecordCommand;
import com.example.studentmanagement.designpattern.command.ExportPaymentRecordCommand;
import com.example.studentmanagement.designpattern.command.ExportPaymentRecordToExcelCommand;
import com.example.studentmanagement.designpattern.command.PaymentCommand;
import com.example.studentmanagement.designpattern.command.PaymentInvoker;
import com.example.studentmanagement.designpattern.command.StatisticsPaymentRecordCommand;
import com.example.studentmanagement.dto.cashier.InvoiceDetailDTO;
import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import com.example.studentmanagement.dto.cashier.PaymentRequestDTO;
import com.example.studentmanagement.dto.cashier.PaymentStatisticsDTO;
import com.example.studentmanagement.service.cashier.PaymentReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cashier")
@CrossOrigin(origins = "http://localhost:3000")
public class CashierController {

    @Autowired
    private PaymentReceiptService paymentReceiptService;

    @Autowired
    private PaymentInvoker invoker;

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDetailDTO> getInvoiceDetail(@PathVariable Integer invoiceId) {
        try {
            InvoiceDetailDTO invoice = paymentReceiptService.getInvoiceById(invoiceId);
            if (invoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add-payment")
    public ResponseEntity<String> addPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        try {
            paymentReceiptService.addPaymentRecord(
                paymentRequest.getCashierId(),
                paymentRequest.getInvoiceId(),
                paymentRequest.getAmount()
            );
            return ResponseEntity.ok("Thêm thanh toán thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Thêm thanh toán thất bại: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Thêm thanh toán thất bại: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Thêm thanh toán thất bại: Lỗi hệ thống - " + e.getMessage());
        }
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDetailDTO>> getInvoices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "2024-2025") String academicYear) {
        try {
            if (status != null && !List.of("Đã thanh toán", "Chưa thanh toán", "Thanh toán 1 phần").contains(status)) {
                return ResponseEntity.badRequest().body(null);
            }
            if (academicYear != null && !academicYear.matches("\\d{4}-\\d{4}")) {
                return ResponseEntity.badRequest().body(null);
            }

            List<InvoiceDetailDTO> invoices = paymentReceiptService.getAllInvoices(status, academicYear);
            return ResponseEntity.ok(invoices);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/payment-records")
    public ResponseEntity<List<PaymentRecordDTO>> getPaymentRecords(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String status) {
        try {
            List<PaymentRecordDTO> records = paymentReceiptService.exportPaymentRecords(academicYear, status);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export-payment-records")
    public ResponseEntity<byte[]> exportPaymentRecords(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String status) {
        try {
            if (status != null && !status.isEmpty() &&
                    !status.equals("Đã thanh toán") &&
                    !status.equals("Chưa thanh toán") &&
                    !status.equals("Thanh toán 1 phần")) {
                throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
            }

            PaymentCommand exportDataCommand = new ExportPaymentRecordCommand(paymentReceiptService, academicYear, status);
            invoker.addCommand(exportDataCommand);
            invoker.runCommands();
            List<PaymentRecordDTO> records = ((ExportPaymentRecordCommand) exportDataCommand).getResult();

            PaymentCommand exportExcelCommand = new ExportPaymentRecordToExcelCommand(records);
            invoker.addCommand(exportExcelCommand);
            invoker.runCommands();
            byte[] excelBytes = ((ExportPaymentRecordToExcelCommand) exportExcelCommand).getResult();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "payment-records.xlsx");
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/statistics-payment-records")
    public ResponseEntity<PaymentStatisticsDTO> statisticsPaymentRecords(@RequestParam String academicYear) {
        try {
            PaymentCommand statsCommand = new StatisticsPaymentRecordCommand(paymentReceiptService, academicYear);
            invoker.addCommand(statsCommand);
            invoker.runCommands();

            PaymentStatisticsDTO stats = ((StatisticsPaymentRecordCommand) statsCommand).getResult();
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
