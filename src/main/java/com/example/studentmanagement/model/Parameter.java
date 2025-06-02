package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parameter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parameter_name", length = 50, nullable = false, unique = true)
    private String parameterName;

    @Column(nullable = false)
    private Float value;
}
