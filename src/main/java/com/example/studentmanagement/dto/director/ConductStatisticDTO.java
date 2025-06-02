package com.example.studentmanagement.dto.director;

import lombok.Data;

@Data
public class ConductStatisticDTO {
    private int goodCount;      // Số học sinh hạnh kiểm Tốt
    private int fairCount;      // Số học sinh hạnh kiểm Khá
    private int averageCount;   // Số học sinh hạnh kiểm Trung bình
    private int poorCount;      // Số học sinh hạnh kiểm Yếu
}