package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Supervisor;

public class SupervisorFactory implements UserFactory {
    @Override
    public Object create(UserRequest request, Account account) {
        Supervisor supervisor = new Supervisor(request.getId());
        supervisor.setAccount(account);
        return supervisor;
    }
}