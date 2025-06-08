package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.CashierRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.enums.WorkingStatus;
import org.springframework.stereotype.Component;

@Component
public class CashierFactory implements UserFactory<Cashier, CashierRequest> {
    @Override
    public Cashier create(CashierRequest request, Account account) {
        Cashier cashier = new Cashier();
        cashier.setAccount(account);
        cashier.setStatus(WorkingStatus.valueOf(request.getStatus()));
        return cashier;
    }
}