package com.example.studentmanagement.controller;

import com.example.studentmanagement.designpattern.command.AddPaymentRecordCommand;
import com.example.studentmanagement.designpattern.command.ExportPaymentRecordCommand;
import com.example.studentmanagement.designpattern.command.ExportPaymentRecordToExcelCommand;
import com.example.studentmanagement.designpattern.command.PaymentCommand;
import com.example.studentmanagement.designpattern.command.PaymentInvoker;
import com.example.studentmanagement.designpattern.command.StatisticsPaymentRecordCommand;
import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import com.example.studentmanagement.dto.cashier.PaymentStatisticsDTO;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.repository.CashierRepository;
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
public class CashierController {

    @Autowired
    private PaymentReceiptService paymentReceiptService;

    @Autowired
    private CashierRepository cashierRepository;

    @Autowired
    private PaymentInvoker invoker;

    @PostMapping("/add-payment")
    public ResponseEntity<String> addPayment(
            @RequestParam String cashierId,
            @RequestParam String invoiceId,
            @RequestParam float amount) {
        try {
            Cashier cashier = cashierRepository.findById(cashierId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân: " + cashierId));

            PaymentCommand addCommand = new AddPaymentRecordCommand(cashier, paymentReceiptService, invoiceId, amount);
            invoker.addCommand(addCommand);
            invoker.runCommands();

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
            // Kiểm tra trạng thái hợp lệ
            if (status != null && !status.isEmpty() && !status.equals("Đã thanh toán") && !status.equals("Chưa thanh toán") && !status.equals("Thanh toán 1 phần")) {
                throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
            }

            // Thực thi command để lấy dữ liệu thô
            PaymentCommand exportDataCommand = new ExportPaymentRecordCommand(paymentReceiptService, academicYear, status);
            invoker.addCommand(exportDataCommand);
            invoker.runCommands();
            List<PaymentRecordDTO> records = ((ExportPaymentRecordCommand) exportDataCommand).getResult();

            // Thực thi command để xuất Excel
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