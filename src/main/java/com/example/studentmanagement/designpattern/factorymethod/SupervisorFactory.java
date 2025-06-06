package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.enums.WorkingStatus;
import org.springframework.stereotype.Component;

@Component
public class SupervisorFactory implements UserFactory {

    @Override
    public Object create(UserRequest request, Account account) {
        Supervisor supervisor = new Supervisor();
        supervisor.setAccount(account);
        supervisor.setStatus(WorkingStatus.Working);
        return supervisor;
    }
}