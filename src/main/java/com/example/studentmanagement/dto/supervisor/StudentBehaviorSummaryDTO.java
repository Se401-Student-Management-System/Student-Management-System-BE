
package com.example.studentmanagement.dto.supervisor;


public class StudentBehaviorSummaryDTO {
    private String studentId;
    private String fullName;
    private String className;
    private Long violationCount;
    private Integer behaviorScore;
    private String status;

    // Getters & Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Long getViolationCount() { return violationCount; }
    public void setViolationCount(Long violationCount) { this.violationCount = violationCount; }
    public Integer getBehaviorScore() { return behaviorScore; }
    public void setBehaviorScore(Integer behaviorScore) { this.behaviorScore = behaviorScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
