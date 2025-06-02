package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.AssignmentRole;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "assignment",
    uniqueConstraints = @UniqueConstraint(columnNames = {
        "teacher_id", "subject_id", "class_id", "semester", "academic_year"
    })
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class clazz;

    @Column
    private Integer semester;

    @Column(name = "academic_year", length = 9, nullable = false)
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentRole role;
}
