package com.example.studentmanagement.designpattern.iterator;

import com.example.studentmanagement.model.Student;

import java.util.List;

public class StudentCollection implements IStudentCollection {
    private List<Student> students;

    public StudentCollection(List<Student> students) {
        this.students = students;
    }
    public List<Student> getStudents() {
        return students;
    }

    @Override
    public IStudentIterator createIterator() {
        return new StudentIterator(this);
    }

}
