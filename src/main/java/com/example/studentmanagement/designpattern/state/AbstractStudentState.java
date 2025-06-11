package com.example.studentmanagement.designpattern.state;

import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;

public abstract class AbstractStudentState implements  StudentState{
    protected Student student;

    public AbstractStudentState(Student student) {
        this.student = student;
    }

    @Override
    public void study(Student student) {
        System.out.println("Default: Cannot perform study action in " + getStatusName().name() + " state.");
    }

    @Override
    public void suspend(Student student) {
        System.out.println("Default: Cannot suspend student in " + getStatusName().name() + " state.");
    }

    @Override
    public void warn(Student student) {
        System.out.println("Default: Cannot warn student in " + getStatusName().name() + " state.");
    }

    @Override
    public void activate(Student student) {
        System.out.println("Default: Cannot activate student in " + getStatusName().name() + " state.");
    }

    @Override
    public void enroll(Student student) {
        System.out.println("Default: Cannot enroll student in " + getStatusName().name() + " state.");
    }

    @Override
    public void leave(Student student) {
        System.out.println("Default: Cannot leave school in " + getStatusName().name() + " state.");
    }

    public abstract StudyStatus getStatusName();

}
