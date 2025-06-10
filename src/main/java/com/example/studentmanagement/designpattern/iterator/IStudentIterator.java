package com.example.studentmanagement.designpattern.iterator;

import com.example.studentmanagement.model.Student;

public interface IStudentIterator {
    boolean hasMore();

    Student getNext();
}
