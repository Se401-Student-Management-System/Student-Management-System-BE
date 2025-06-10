package com.example.studentmanagement.designpattern.CoR;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.AuthenticationContext;
import com.example.studentmanagement.model.Account;

public class PasswordHandler extends AbstractAuthenticationHandler {
    @Override
    public void handleRequest(LoginRequest request, AuthenticationContext context) throws AuthenticationException {
        Account account = context.getAccount();
        if (!account.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        super.handleRequest(request, context);
    }
}