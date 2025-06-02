package com.example.studentmanagement.designpattern.command;

import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.service.cashier.PaymentReceiptService;

public class AddPaymentRecordCommand implements PaymentCommand {
    private Cashier cashier;
    private PaymentReceiptService service;
    private String invoiceId;
    private float amount;

    public AddPaymentRecordCommand(Cashier cashier, PaymentReceiptService service, String invoiceId, float amount) {
        this.cashier = cashier;
        this.service = service;
        this.invoiceId = invoiceId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        service.addPaymentRecord(invoiceId, cashier.getId(), amount);
    }
}