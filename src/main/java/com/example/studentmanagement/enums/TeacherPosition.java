package com.example.studentmanagement.enums;

public enum TeacherPosition {
    HEAD("Tổ trưởng"),
    DEPUTY("Tổ phó"),
    TEACHER("Giáo viên");

    private final String label;

    TeacherPosition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TeacherPosition fromLabel(String label) {
        for (TeacherPosition p : values()) {
            if (p.label.equalsIgnoreCase(label)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy chức vụ: " + label);
    }
}
