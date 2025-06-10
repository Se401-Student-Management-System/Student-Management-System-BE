package com.example.studentmanagement.designpattern.CoR;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.AuthenticationContext;

public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
    protected AuthenticationHandler nextHandler;

    @Override
    public void setNextHandler(AuthenticationHandler next) {
        this.nextHandler = next;
    }

    @Override
    public void handleRequest(LoginRequest request, AuthenticationContext context) throws AuthenticationException {
        if (nextHandler != null) {
            nextHandler.handleRequest(request, context);
        }
    }
}