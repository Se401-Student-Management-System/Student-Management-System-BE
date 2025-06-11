package com.example.studentmanagement.model;

import com.example.studentmanagement.enums.WorkingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cashier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cashier implements UserEntity {

    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkingStatus status;

    // Thêm constructor chỉ nhận id
    public Cashier(String id) {
        this.id = id;
        this.status = WorkingStatus.Working;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public WorkingStatus getStatus() { return status; }
    public void setStatus(WorkingStatus status) { this.status = status; }
}