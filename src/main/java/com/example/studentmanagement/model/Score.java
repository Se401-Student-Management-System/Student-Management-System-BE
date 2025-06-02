package com.example.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Transient;

@Entity
@Table(name = "score", uniqueConstraints = @UniqueConstraint(columnNames = {
        "student_id", "subject_id", "semester", "academic_year"
}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // === Quan hệ khóa ngoại ===

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    // === Các điểm số ===

    @Column(name = "score_15m_1")
    private Float score15m1;

    @Column(name = "score_15m_2")
    private Float score15m2;

    @Column(name = "score_1h_1")
    private Float score1h1;

    @Column(name = "score_1h_2")
    private Float score1h2;

    @Column(name = "final_score")
    private Float finalScore;

    // === Học kỳ, năm học, ghi chú ===

    @Column
    private Integer semester; // 1 hoặc 2

    @Column(name = "academic_year", length = 9)
    private String academicYear; // dạng "2024-2025"

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Transient
    private String fieldName; // Không lưu trong DB, chỉ dùng khi gửi thông báo

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
