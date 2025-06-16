package com.example.studentmanagement.designpattern.proxy;

import com.example.studentmanagement.model.UserEntity;

public interface GradeInterface {
    String getId();
    String getRole();
    boolean isAuthorized(UserEntity user);
}