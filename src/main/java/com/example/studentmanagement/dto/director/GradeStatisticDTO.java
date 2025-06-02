package com.example.studentmanagement.dto.director;

import lombok.Data;

@Data
public class GradeStatisticDTO {
    private int excellentCount;
    private double excellentChange; // Phần trăm chênh lệch học sinh Giỏi
    private int goodCount;
    private double goodChange;      // Phần trăm chênh lệch học sinh Khá
    private int averageCount;
    private double averageChange;   // Phầnრ: Phần trăm chênh lệch học sinh Trung bình
    private double weakCount;
    private double weakChange;      // Phần trăm chênh lệch học sinh Yếu
}