package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Teacher;

public class TeacherFactory implements UserFactory {
    @Override
    public Object create(UserRequest request, Account account) {
        Teacher Teacher = new Teacher(request.getId());
        Teacher.setAccount(account);
        return Teacher;
    }
}
