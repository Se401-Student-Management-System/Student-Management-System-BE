package com.example.studentmanagement.dto.director;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String gender;
    private String BirthDate;
    private String role;
    private String entity; // (CASHIER, SUPERVISOR, TEACHER, STUDENT,...)
}