package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "class")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_name", length = 255, nullable = false, unique = true)
    private String className;

    public String getClassName() {
        return className;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
