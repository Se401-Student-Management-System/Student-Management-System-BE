package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.TeacherRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.enums.TeacherPosition;
import com.example.studentmanagement.enums.TeachingStatus;
import org.springframework.stereotype.Component;

@Component
public class TeacherFactory implements UserFactory<Teacher, TeacherRequest> {
    @Override
    public Teacher create(TeacherRequest request, Account account) {
        Teacher teacher = new Teacher();
        teacher.setAccount(account);
        teacher.setStatus(TeachingStatus.valueOf(request.getStatus()));
        teacher.setPosition(TeacherPosition.valueOf(request.getPosition()));
        return teacher;
    }
}