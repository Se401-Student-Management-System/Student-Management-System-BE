package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.SupervisorRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.enums.WorkingStatus;
import org.springframework.stereotype.Component;

@Component
public class SupervisorFactory implements UserFactory<Supervisor, SupervisorRequest> {
    @Override
    public Supervisor create(SupervisorRequest request, Account account) {
        Supervisor supervisor = new Supervisor();
        supervisor.setAccount(account);
        supervisor.setStatus(WorkingStatus.valueOf(request.getStatus()));
        return supervisor;
    }
}