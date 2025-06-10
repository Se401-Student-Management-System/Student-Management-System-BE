package com.example.studentmanagement.dto;

import com.example.studentmanagement.model.Account;

public class AuthenticationContext {
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}