package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.StudyStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(length = 50)
    private String ethnicity;

    @Column(name = "birth_place", length = 100)
    private String birthPlace;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    public Student(String id) {
        this.id = id;
        this.status = StudyStatus.ACTIVE;
    }
}