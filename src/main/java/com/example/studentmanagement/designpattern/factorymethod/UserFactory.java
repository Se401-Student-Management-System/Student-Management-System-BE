package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;

public interface UserFactory {
    Object create(UserRequest request, Account account);
}