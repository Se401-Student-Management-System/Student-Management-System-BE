package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "title")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title_name", length = 100, nullable = false, unique = true)
    private String titleName;

    @Column(name = "min_avg_score")
    private Float minAvgScore;

    @Column(name = "min_behavior_score")
    private Float minBehaviorScore;
}
