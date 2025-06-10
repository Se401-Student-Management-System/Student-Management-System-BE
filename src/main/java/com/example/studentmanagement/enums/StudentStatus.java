package com.example.studentmanagement.enums;

public enum StudentStatus {
    ACTIVE("ACTIVE"),
    PENDING("PENDING"),
    WARNING("WARNING"),
    INACTIVE("INACTIVE");

    private final String displayName;

    StudentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StudentStatus fromDisplayName(String displayName) {
        for (StudentStatus status : StudentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Trạng thái sinh viên không hợp lệ: " + displayName);
    }

}
