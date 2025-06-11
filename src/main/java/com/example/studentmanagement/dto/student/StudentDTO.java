package com.example.studentmanagement.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate birthDate;
    private String roleName;
    private String ethnicity;
    private String birthPlace;
    private String status;

    private String className;
    private String academicYear;
}
