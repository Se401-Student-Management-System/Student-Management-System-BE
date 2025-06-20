package com.example.studentmanagement.model;

import com.example.studentmanagement.converter.TeacherPositionConverter;
import com.example.studentmanagement.converter.TeachingStatusConverter;
import com.example.studentmanagement.enums.TeacherPosition;
import com.example.studentmanagement.enums.TeachingStatus;
import com.example.studentmanagement.designpattern.proxy.GradeInterface;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity
@Table(name = "teacher")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Teacher implements GradeInterface, UserEntity {
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

    public Teacher(String id) {
        this.id = id;
        this.status = TeachingStatus.DANG_GIANG_DAY;
    }

    @Override
    public String getRole(){
        return "TEACHER";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public TeachingStatus getStatus() { return status; }
    public void setStatus(TeachingStatus status) { this.status = status; }

    public TeacherPosition getPosition() { return position; }
    public void setPosition(TeacherPosition position) { this.position = position; }

    @Override
    public boolean isAuthorized(UserEntity user) {
        if (user instanceof Teacher) {
            return this.getId().equals(((Teacher) user).getId());
        }
        if (user instanceof Student) {
            return false;
        }
        return false;
    }

    public void checkIfTeaching(Student student) {
        boolean isTeaching = student.getScores().stream()
            .anyMatch(score -> score.getTeacher().getId().equals(this.getId()));

        if (!isTeaching) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không dạy học sinh này, không thể xem điểm.");
        }
    }
}