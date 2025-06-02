package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "student_class",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "class_id", "academic_year"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class clazz; // Dùng "clazz" thay vì "class" để tránh conflict từ khóa

    @Column(name = "academic_year", length = 9, nullable = false)
    private String academicYear;
}
