package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.model.Account;

public interface UserFactory<T, R> {
    T create(R request, Account account);
}