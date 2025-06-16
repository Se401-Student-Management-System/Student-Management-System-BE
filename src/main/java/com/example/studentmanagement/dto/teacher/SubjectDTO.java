package com.example.studentmanagement.dto.teacher;

public class SubjectDTO {
    private String id;
    private String subjectName;
    private String className;
    
    public SubjectDTO(String id, String subjectName, String className) {
        this.id = id;
        this.subjectName = subjectName;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}