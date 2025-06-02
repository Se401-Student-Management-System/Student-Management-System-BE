package com.example.studentmanagement.designpattern.command;

import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import com.example.studentmanagement.service.cashier.PaymentReceiptService;

import java.util.List;

public class ExportPaymentRecordCommand implements PaymentCommand {
    private PaymentReceiptService service;
    private String academicYear;
    private String status;
    private List<PaymentRecordDTO> result;

    public ExportPaymentRecordCommand(PaymentReceiptService service, String academicYear, String status) {
        this.service = service;
        this.academicYear = academicYear;
        this.status = status;
    }

    @Override
    public void execute() {
        result = service.exportPaymentRecords(academicYear, status);
    }

    public List<PaymentRecordDTO> getResult() {
        return result;
    }
}