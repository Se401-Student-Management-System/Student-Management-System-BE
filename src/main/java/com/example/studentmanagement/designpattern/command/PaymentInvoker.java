package com.example.studentmanagement.designpattern.command;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("session")
public class PaymentInvoker {
    private List<PaymentCommand> commands = new ArrayList<>();

    public void addCommand(PaymentCommand command) {
        commands.add(command);
    }

    public void runCommands() throws Exception {
        for (PaymentCommand command : commands) {
            command.execute();
        }
        commands.clear();
    }
}