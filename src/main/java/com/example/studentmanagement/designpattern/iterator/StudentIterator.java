package com.example.studentmanagement.designpattern.iterator;

import com.example.studentmanagement.model.Student;

public class StudentIterator  implements  IStudentIterator{
    private StudentCollection collection;
    private int position = 0;

    public StudentIterator(StudentCollection collection) {
        this.collection = collection;
    }

    @Override
    public boolean hasMore() {
        return position < collection.getStudents().size();
    }

    @Override
    public Student getNext() {
        if (hasMore()) {
            Student student = collection.getStudents().get(position);
            position++;
            return student;
        }
        return null;
    }
}
