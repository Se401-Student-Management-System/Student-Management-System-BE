
package com.example.studentmanagement.designpattern.factorymethod;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.enums.TeachingStatus;
import com.example.studentmanagement.enums.TeacherPosition;
import org.springframework.stereotype.Component;

@Component
public class TeacherFactory implements UserFactory {

    @Override
    public Object create(UserRequest request, Account account) {
        Teacher teacher = new Teacher();
        teacher.setAccount(account);
        teacher.setStatus(TeachingStatus.DANG_GIANG_DAY);
        teacher.setPosition(TeacherPosition.TEACHER);
        return teacher;
    }
}