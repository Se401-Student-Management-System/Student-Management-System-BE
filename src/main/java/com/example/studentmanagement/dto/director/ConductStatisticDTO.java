package com.example.studentmanagement.dto.director;

public class ConductStatisticDTO {

    private int goodCount;      // Số học sinh hạnh kiểm Tốt
    private int fairCount;      // Số học sinh hạnh kiểm Khá
    private int averageCount;   // Số học sinh hạnh kiểm Trung bình
    private int poorCount;      // Số học sinh hạnh kiểm Yếu

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getFairCount() {
        return fairCount;
    }

    public void setFairCount(int fairCount) {
        this.fairCount = fairCount;
    }

    public int getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(int averageCount) {
        this.averageCount = averageCount;
    }

    public int getPoorCount() {
        return poorCount;
    }

    public void setPoorCount(int poorCount) {
        this.poorCount = poorCount;
    }
}
