package com.example.studentmanagement.dto.director;

public class LowestConductStudentDTO {

    private String studentId;
    private String fullName;
    private String className;
    private Integer behaviorScore;
    private String status;
    private Long violationCount;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getBehaviorScore() {
        return behaviorScore;
    }

    public void setBehaviorScore(Integer behaviorScore) {
        this.behaviorScore = behaviorScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(Long violationCount) {
        this.violationCount = violationCount;
    }
}
