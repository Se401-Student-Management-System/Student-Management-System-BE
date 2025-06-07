package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.enums.StudyStatus;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements UserFactory {

    @Override
    public Object create(UserRequest request, Account account) {
        Student student = new Student();
        student.setAccount(account);
        student.setStatus(StudyStatus.ACTIVE);
        student.setBirthPlace(account.getAddress() != null ? account.getAddress() : "Unknown");
        student.setEthnicity("Kinh");
        return student;
    }
}