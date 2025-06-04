package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements UserFactory {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Object create(UserRequest request, Account account) {
        Student student = new Student();
        student.setAccount(account);
        // Thêm các trường đặc trưng nếu cần
        

        return studentRepository.save(student);
    }
}