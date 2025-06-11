package com.example.studentmanagement.designpattern.CoR;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.AuthenticationContext;

public interface AuthenticationHandler {
    void handleRequest(LoginRequest request, AuthenticationContext context) throws AuthenticationException;
    void setNextHandler(AuthenticationHandler next);
}