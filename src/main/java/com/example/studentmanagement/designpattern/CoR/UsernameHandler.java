package com.example.studentmanagement.designpattern.CoR;

import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.dto.AuthenticationContext;
import com.example.studentmanagement.dto.LoginRequest;

public class UsernameHandler extends AbstractAuthenticationHandler {
    private final AccountRepository accountRepository;

    public UsernameHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void handleRequest(LoginRequest request, AuthenticationContext context) throws AuthenticationException {
        if (!accountRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username does not exist");
        }

        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Failed to retrieve account"));
        context.setAccount(account);

        super.handleRequest(request, context);
    }
}