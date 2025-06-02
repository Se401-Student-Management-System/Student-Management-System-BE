package com.example.studentmanagement.designpattern.command;

import com.example.studentmanagement.dto.cashier.PaymentStatisticsDTO;
import com.example.studentmanagement.service.cashier.PaymentReceiptService;

public class StatisticsPaymentRecordCommand implements PaymentCommand {
    private PaymentReceiptService service;
    private String academicYear;
    private PaymentStatisticsDTO result;

    public StatisticsPaymentRecordCommand(PaymentReceiptService service, String academicYear) {
        this.service = service;
        this.academicYear = academicYear;
    }

    @Override
    public void execute() {
        result = service.getPaymentStatistics(academicYear);
    }

    public PaymentStatisticsDTO getResult() {
        return result;
    }
}