package com.example.studentmanagement.service;

import org.springframework.stereotype.Service;

import com.example.studentmanagement.designpattern.CoR.AuthenticationHandler;
import com.example.studentmanagement.designpattern.CoR.PasswordHandler;
import com.example.studentmanagement.designpattern.CoR.UsernameHandler;
import com.example.studentmanagement.designpattern.CoR.RoleHandler;
import com.example.studentmanagement.dto.AuthenticationContext;
import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.designpattern.CoR.AuthenticationException;

@Service
public class AuthenticationService {
    private final AuthenticationHandler authenticationChain;

    public AuthenticationService(AccountRepository accountRepository) {
        // Set up the chain: Username -> Password -> Role
        AuthenticationHandler usernameHandler = new UsernameHandler(accountRepository);
        AuthenticationHandler passwordHandler = new PasswordHandler();
        AuthenticationHandler roleHandler = new RoleHandler();

        usernameHandler.setNextHandler(passwordHandler);
        passwordHandler.setNextHandler(roleHandler);

        this.authenticationChain = usernameHandler;
    }

    public AuthenticationContext authenticate(LoginRequest request) throws AuthenticationException {
        AuthenticationContext context = new AuthenticationContext();
        authenticationChain.handleRequest(request, context);
        return context;
    }
}