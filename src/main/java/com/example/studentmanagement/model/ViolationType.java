package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "violation_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "violation_name", length = 255, nullable = false, unique = true)
    private String violationName;

    @Column(name = "deducted_points")
    private Float deductedPoints;
}
