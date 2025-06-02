package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.Position;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_of_directors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardOfDirectors {

    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Position position;
}
