package com.example.studentmanagement.dto.director;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthDate;
    private String roleId;
    private String entity; // "Student", "Teacher", "Cashier", "Supervisor"
}