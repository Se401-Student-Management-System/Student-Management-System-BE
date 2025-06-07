package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.WorkingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supervisor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor {

    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkingStatus status;

    public Supervisor(String id) {
        this.id = id;
        this.status = WorkingStatus.Working;
    }
}
