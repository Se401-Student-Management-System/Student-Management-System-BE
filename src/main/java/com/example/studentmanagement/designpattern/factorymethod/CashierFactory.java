package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.enums.WorkingStatus;
import org.springframework.stereotype.Component;

@Component
public class CashierFactory implements UserFactory {

    @Override
    public Object create(UserRequest request, Account account) {
        Cashier cashier = new Cashier();
        cashier.setAccount(account);
        cashier.setStatus(WorkingStatus.Working);
        return cashier;
    }
}