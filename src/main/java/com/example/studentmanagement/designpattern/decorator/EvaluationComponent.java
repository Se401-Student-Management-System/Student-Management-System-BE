package com.example.studentmanagement.designpattern.decorator;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;

public interface EvaluationComponent {
    void addComment(Score score, Teacher teacher, String comment);
}