package com.example.studentmanagement.enums;

public enum TeachingStatus {
    DANG_GIANG_DAY("Đang giảng dạy"),
    THOI_VIEC("Thôi việc");

    private final String label;

    TeachingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TeachingStatus fromLabel(String label) {
        for (TeachingStatus status : values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không có trạng thái: " + label);
    }
}
