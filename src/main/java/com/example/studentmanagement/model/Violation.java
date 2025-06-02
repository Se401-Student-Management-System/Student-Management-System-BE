package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "violation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Violation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class clazz;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
    private Supervisor supervisor;

    @ManyToOne
    @JoinColumn(name = "violation_type_id", referencedColumnName = "id")
    private ViolationType violationType;

    @Column
    private Integer semester;

    @Column(name = "academic_year", length = 9)
    private String academicYear;

    @Column(name = "violation_date")
    private LocalDate violationDate;
}
