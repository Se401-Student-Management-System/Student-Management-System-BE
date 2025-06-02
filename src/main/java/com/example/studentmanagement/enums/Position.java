package com.example.studentmanagement.enums;

public enum Position {
    Principal("Hiệu trưởng"),
    Vice_Principal("Phó hiệu trưởng");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}