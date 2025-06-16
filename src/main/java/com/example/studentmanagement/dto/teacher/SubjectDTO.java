package com.example.studentmanagement.dto.teacher;

public class SubjectDTO {
    private String id;
    private String subjectName;
    
    public SubjectDTO(String id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
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
}
