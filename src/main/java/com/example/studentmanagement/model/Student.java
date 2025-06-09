package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.StudyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements UserEntity {
    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnoreProperties("student")
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
        this.status = StudyStatus.PENDING;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public StudyStatus getStatus() { return status; }
    public void setStatus(StudyStatus status) { this.status = status; }
    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }
    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
}