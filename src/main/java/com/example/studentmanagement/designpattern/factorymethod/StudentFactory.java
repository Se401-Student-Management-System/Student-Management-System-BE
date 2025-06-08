package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.StudentRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.enums.StudyStatus;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements UserFactory<Student, StudentRequest> {
    @Override
    public Student create(StudentRequest request, Account account) {
        Student student = new Student();
        student.setAccount(account);
        student.setBirthPlace(request.getBirthPlace());
        student.setEthnicity(request.getEthnicity());
        student.setStatus(StudyStatus.valueOf(request.getStatus()));
        return student;
    }
}