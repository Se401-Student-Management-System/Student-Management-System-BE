package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;

public class StudentFactory implements UserFactory {
    @Override
    public Object create(UserRequest request, Account account) {
        Student student = new Student(request.getId());
        student.setAccount(account);
        return student;
    }
}