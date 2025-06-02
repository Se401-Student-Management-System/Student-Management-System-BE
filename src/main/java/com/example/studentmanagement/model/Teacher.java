package com.example.studentmanagement.model;

import com.example.studentmanagement.converter.TeacherPositionConverter;
import com.example.studentmanagement.converter.TeachingStatusConverter;
import com.example.studentmanagement.enums.TeacherPosition;
import com.example.studentmanagement.enums.TeachingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Convert(converter = TeacherPositionConverter.class) // <- BẮT BUỘC
    @Column(nullable = false)
    private TeacherPosition position;

    @Convert(converter = TeachingStatusConverter.class) // <- BẮT BUỘC
    @Column(nullable = false)
    private TeachingStatus status;

}
