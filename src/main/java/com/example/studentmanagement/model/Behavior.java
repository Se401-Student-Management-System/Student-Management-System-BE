package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "behavior",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "semester", "academic_year"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Behavior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "academic_year", length = 9, nullable = false)
    private String academicYear;

    @Column(name = "behavior_score")
    private Integer behaviorScore = 100;

    @Column(length = 50)
    private String status; // Good, Fair, etc.
}
