package com.example.studentmanagement.model;

import com.example.studentmanagement.designpattern.state.ActiveState;
import com.example.studentmanagement.designpattern.state.InactiveState;
import com.example.studentmanagement.designpattern.state.PendingState;
import com.example.studentmanagement.designpattern.state.StudentState;
import com.example.studentmanagement.designpattern.state.StudentStateFactory;
import com.example.studentmanagement.designpattern.state.WarningState;
import com.example.studentmanagement.enums.StudyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements UserEntity {
    @Id
    @Column(length = 10)
    private String id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnoreProperties("student")
    private Account account;

    @Column(length = 50)
    private String ethnicity;

    @Column(name = "birth_place", length = 100)
    private String birthPlace;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @Transient // Không lưu vào DB
    private StudentState currentState;

    @Transient
    private ActiveState activeState;
    @Transient
    private PendingState pendingState;
    @Transient
    private WarningState warningState;
    @Transient
    private InactiveState inactiveState;

    public Student(String id, Account account, String ethnicity, String birthPlace, StudyStatus status) {
        this.id = id;
        this.account = account;
        this.ethnicity = ethnicity;
        this.birthPlace = birthPlace;
        this.status = status;

        // Khởi tạo các thể hiện của các trạng thái cụ thể
        initStates();
    }

    public Student(String id) {
        this.id = id;
        this.status = StudyStatus.PENDING;
        initStates(); // Khởi tạo các state objects
        setInitialState(); // Thiết lập currentState
    }

    private void initStates() {
        this.activeState = new ActiveState(this);
        this.pendingState = new PendingState(this);
        this.warningState = new WarningState(this);
        this.inactiveState = new InactiveState(this);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void setInitialState() {
        if (this.activeState == null) {
            initStates();
        }
        if (this.status == null) {
            this.status = StudyStatus.PENDING;
        }
        this.currentState = StudentStateFactory.getState(this.status, this); // Truyền StudyStatus trực tiếp
    }

    @PrePersist
    @PreUpdate
    public void prepareForPersist() {
        if (this.currentState == null) {
            setInitialState();
        }
    }

    public void setCurrentState(StudentState newState) {
        this.currentState = newState;
        if (newState != null) {
            this.status = newState.getStatusName();
        }
    }

    public StudentState getCurrentState() {
        return currentState;
    }

    // Đây là nơi Student ủy quyền hành động cho đối tượng trạng thái hiện tại
    public void performStudy() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.study(this);
    }

    public void performSuspend() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.suspend(this);
    }

    public void performWarn() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.warn(this);
    }

    public void performActivate() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.activate(this);
    }

    public void performEnroll() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.enroll(this);
    }

    public void performLeave() {
        if (this.currentState == null)
            setInitialState();
        this.currentState.leave(this);
    }

    public ActiveState getActiveState() {
        return activeState;
    }

    public PendingState getPendingState() {
        return pendingState;
    }

    public WarningState getWarningState() {
        return warningState;
    }

    public InactiveState getInactiveState() {
        return inactiveState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public void setStatus(StudyStatus status) {
        this.status = status;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }
}
