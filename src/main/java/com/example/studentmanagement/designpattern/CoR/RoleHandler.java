package com.example.studentmanagement.designpattern.CoR;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.dto.AuthenticationContext;

public class RoleHandler extends AbstractAuthenticationHandler {
    @Override
    public void handleRequest(LoginRequest request, AuthenticationContext context) throws AuthenticationException {
        Account account = context.getAccount();
        if (account == null || account.getRole() == null) {
            throw new AuthenticationException("Account or role not found");
        }
        if (!account.getRole().getRoleName().equalsIgnoreCase(request.getRole())) {
            throw new AuthenticationException("Invalid role");
        }
        super.handleRequest(request, context);
    }
}