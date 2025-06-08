package com.example.studentmanagement.dto.director;

public class StudentRequest extends UserRequest {
    private String birthPlace;
    private String ethnicity;
    private String status;

    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }

    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}